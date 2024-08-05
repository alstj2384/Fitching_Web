package ssamppong.fitchingWeb.global.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ssamppong.fitchingWeb.dto.UserResponseDto;
import ssamppong.fitchingWeb.entity.User;
import ssamppong.fitchingWeb.global.ResponseUtil;
import ssamppong.fitchingWeb.global.oauth2.PrincipalDetails;
import ssamppong.fitchingWeb.global.jwt.service.JwtService;
import ssamppong.fitchingWeb.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final ResponseUtil responseUtil;
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

            Optional<User> findUser = userRepository.findByEmail(((PrincipalDetails) authentication.getPrincipal()).getUsername());
            User user = null;

            if(findUser.isPresent()){
                user = findUser.get();
                user.updateRefreshToken(refreshToken);
                userRepository.save(user);
            }

            // 객체 반환
            UserResponseDto userResponseDto = new UserResponseDto(user.getUserId(), user.getName());
            responseUtil.setSuccessResponse(response, userResponseDto);
        } catch(Exception e){
            throw e;
        }
    }
}
