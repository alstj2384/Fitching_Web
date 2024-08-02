package ssamppong.fitchingWeb.global.jwt.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import ssamppong.fitchingWeb.global.jwt.util.PasswordUtil;
import ssamppong.fitchingWeb.entity.User;
import ssamppong.fitchingWeb.global.jwt.ErrorCode;
import ssamppong.fitchingWeb.global.jwt.service.JwtService;
import ssamppong.fitchingWeb.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

/**
 * JWT 인증 필터
 * "/login" 이외의 URI 요청을 처리
 *
 * # 요청 흐름
 * - 기본적으로 사용자는 요청 헤더에 AccessToken만 담아서 요청
 * - AccessToken 만료 시 RefreshToken을 요청 헤더에 AccessToken과 함께 요청
 *
 * # 응답 흐름
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우
 * - 인증 성공, RefreshToken 재발급 x
 * 2. RefreshToken이 없고, AccessToken이 없거나 유효하지 않은 경우
 * - 인증 실패, 403 ERROR
 * 3. RefreshToken이 있는 경우
 * - 인증 실패처리, DB의 RefreshToken과 비교하여 일치하면 AccessToken과 RefreshToken 재발급
 */

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String NO_CHECK_URL = "/login";

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // /login 요청이 들어오면 다음 필터 호출
            return; // 이후 필터 진행 막기
        }

        try {

            // RefreshToken 추출
            // 토큰이 없거나 유효하지 않다면
            String refreshToken = jwtService.extractRefreshToken(request).orElse(null);

            // RefreshToken이 요청 헤더에 존재하면, DB의 RefreshToken과 일치 여부 확인 후
            // 일치한다면 AccessToken 재발급
            if (refreshToken != null) {
                if(jwtService.validateToken(refreshToken))
                    checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
                return;
            }

            // RefreshToken이 요청 헤더에 존재하지 않염ㄴ AccessToken을 검사하고 인증
            // AccessToken이 없거나 유효하지 않으면, 인증 객체 없이 다음 필터로 넘어가므로 403에러 발생
            // AccessToken이 유효하다면, 인증객체가 다음 필터로 넘어가므로 인증 성공
            if (refreshToken == null) {
                checkAccessTokenAndAuthentication(request, response, filterChain);
                return;
            }


        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN.getCode());
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getCode());
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN.getCode());
        } catch (Exception e) {
            request.setAttribute("exception", ErrorCode.UNKNOWN_ERROR.getCode());
        }
        filterChain.doFilter(request, response);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        log.info("RefreshToken 재발급");
        Optional<User> findRefreshToken = userRepository.findByRefreshToken(refreshToken);
        if(findRefreshToken.isEmpty()){
            log.info("잘못된 RefreshToken으로 요청");
            jwtService.setFailureMessage(response);
        } else{
            User user = findRefreshToken.get();
            String reIssuedRefreshToken = reIssueRefreshToken(user);
            String reIssuedAccessToken = jwtService.createAccessToken(user.getEmail());
            jwtService.sendAccessTokenAndRefreshToken(response, reIssuedAccessToken, reIssuedRefreshToken);
            log.info("RefreshToken 재발급 완료");
        }
    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.extractAccessToken(request).orElse(null);
        boolean isValid = false;


        if (accessToken != null) {
            isValid = jwtService.validateToken(accessToken);
        }

        if (isValid) {
            String email = jwtService.extractEmail(accessToken).orElse(null);
            if (email != null) {
                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) {
                    saveAuthentication(user);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(User user) {
        String password = user.getPassword();
        if (password == null) {
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(password)
                .roles(user.getRole())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));

//        log.info("{}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String reIssueRefreshToken(User user) {
        String refreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
        return refreshToken;
    }
}
