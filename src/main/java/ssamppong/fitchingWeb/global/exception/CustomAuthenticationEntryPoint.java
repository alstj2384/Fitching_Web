package ssamppong.fitchingWeb.global.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ssamppong.fitchingWeb.global.ResponseUtil;
import ssamppong.fitchingWeb.global.jwt.ErrorCode;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Integer exception = (Integer) request.getAttribute("exception");


        if(exception == null || exception == ErrorCode.UNKNOWN_ERROR.getCode()){
            ResponseUtil.setResponse(response, ErrorCode.UNKNOWN_ERROR);
            return;
        }

        if(exception == ErrorCode.WRONG_TYPE_TOKEN.getCode()){
            ResponseUtil.setResponse(response, ErrorCode.WRONG_TYPE_TOKEN);
            return;
        }

        if(exception == ErrorCode.EXPIRED_TOKEN.getCode()){
            ResponseUtil.setResponse(response, ErrorCode.EXPIRED_TOKEN);
            return;

        }

        if(exception == ErrorCode.UNSUPPORTED_TOKEN.getCode()){
            ResponseUtil.setResponse(response, ErrorCode.UNSUPPORTED_TOKEN);
            return;
        }

        if(exception == ErrorCode.ACCESS_DENIED.getCode()){
            ResponseUtil.setResponse(response, ErrorCode.ACCESS_DENIED);
            return;
        }

    }

}
