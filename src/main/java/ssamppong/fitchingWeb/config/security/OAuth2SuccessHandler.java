package ssamppong.fitchingWeb.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ssamppong.fitchingWeb.jwt.JwtTokenProvider;
import ssamppong.fitchingWeb.jwt.JwtToken;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private static final String URI = "/";
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        JwtToken generate = jwtTokenProvider.generate(authentication);
        log.info("OAuth2 인증 성공");
        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("accessToken", generate.getAccessToken())
                .build().toUriString();
        response.sendRedirect(redirectUrl);
    }
}
