package ssamppong.fitchingWeb.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ssamppong.fitchingWeb.jwt.ErrorCode;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Integer exception = (Integer) request.getAttribute("exception");


        if(exception == ErrorCode.UNKNOWN_ERROR.getCode()){
            setResponse(response, ErrorCode.UNKNOWN_ERROR);
            log.info("{}", ErrorCode.UNKNOWN_ERROR);
            return;
        }

        if(exception == ErrorCode.WRONG_TYPE_TOKEN.getCode()){
            setResponse(response, ErrorCode.WRONG_TYPE_TOKEN);
            log.info("{}", ErrorCode.WRONG_TYPE_TOKEN);
            return;
        }

        if(exception == ErrorCode.EXPIRED_TOKEN.getCode()){
            setResponse(response, ErrorCode.EXPIRED_TOKEN);
            log.info("{}", ErrorCode.EXPIRED_TOKEN);
            return;

        }

        if(exception == ErrorCode.UNSUPPORTED_TOKEN.getCode()){
            setResponse(response, ErrorCode.UNSUPPORTED_TOKEN);
            log.info("{}", ErrorCode.UNSUPPORTED_TOKEN);
            return;
        }

        if(exception == ErrorCode.ACCESS_DENIED.getCode()){
            setResponse(response, ErrorCode.ACCESS_DENIED);
            log.info("{}", ErrorCode.ACCESS_DENIED);
            return;
        }

    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"message\" : \"" + errorCode.getMessage()
                + "\", \"code\" : \"" +  errorCode.getCode()
                + "\", \"errors\" : [ ] }");
    }
}
