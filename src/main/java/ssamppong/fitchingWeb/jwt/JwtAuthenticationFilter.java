package ssamppong.fitchingWeb.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;


    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken((HttpServletRequest) request);
        log.info("JWT 토큰 검증");

        try{
            if(token != null && jwtTokenProvider.validateToken(token)){
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN.getCode());
            log.info("{}", ErrorCode.WRONG_TYPE_TOKEN);
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getCode());
            log.info("{}", ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN.getCode());
            log.info("{}", ErrorCode.UNSUPPORTED_TOKEN);
        } catch (Exception e) {
            request.setAttribute("exception", ErrorCode.UNKNOWN_ERROR.getCode());
            log.info("{}", ErrorCode.UNKNOWN_ERROR);
        }

        filterChain.doFilter(request, response);
    }
}
