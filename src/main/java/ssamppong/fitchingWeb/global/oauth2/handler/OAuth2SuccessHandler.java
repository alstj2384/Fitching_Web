package ssamppong.fitchingWeb.global.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ssamppong.fitchingWeb.global.oauth2.PrincipalDetails;
import ssamppong.fitchingWeb.global.jwt.service.JwtService;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private static final String URI = "/";
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try{
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            String accessToken = jwtService.createAccessToken(principalDetails.getUsername());
            String refreshToken = jwtService.createRefreshToken();

            log.info("로그인에 성공했습니다. 이메일 : {}", principalDetails.getUsername());
            log.info("로그인에 성공했습니다. AccessToken : {}", accessToken);

            jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
            jwtService.updateRefreshToken(principalDetails.getUsername(), refreshToken);
        } catch(Exception e){
            throw e;
        }
    }
}
