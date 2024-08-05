package ssamppong.fitchingWeb.global.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import ssamppong.fitchingWeb.dto.UserResponseDto;
import ssamppong.fitchingWeb.entity.User;
import ssamppong.fitchingWeb.global.ResponseUtil;
import ssamppong.fitchingWeb.global.jwt.service.JwtService;
import ssamppong.fitchingWeb.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final ResponseUtil responseUtil;


    @Value("${jwt.access.expiration")
    private String accessTokenExpiration;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = extractUsername(authentication);
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(email, refreshToken);

        Optional<User> findUser = userRepository.findByEmail(email);
        User user = null;

        if(findUser.isPresent()){
            user = findUser.get();
            user.updateRefreshToken(refreshToken);
            userRepository.save(user);
        }

        // 객체 반환
        UserResponseDto userResponseDto = new UserResponseDto(user.getUserId(), user.getName());
        responseUtil.setSuccessResponse(response, userResponseDto);

        log.info("로그인에 성공했습니다. 이메일 : {}", email);
        log.info("로그인에 성공했습니다. AccessToken : {}", accessToken);

    }

    private String extractUsername(Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

}
