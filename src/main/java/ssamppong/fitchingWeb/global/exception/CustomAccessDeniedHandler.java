package ssamppong.fitchingWeb.global.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import ssamppong.fitchingWeb.global.ResponseUtil;
import ssamppong.fitchingWeb.global.jwt.ErrorCode;

import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ResponseUtil responseUtil;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        responseUtil.setFailureResponse(response, ErrorCode.ACCESS_DENIED);
    }
}
