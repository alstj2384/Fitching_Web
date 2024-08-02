package ssamppong.fitchingWeb.global;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ssamppong.fitchingWeb.global.jwt.ErrorCode;

import java.io.IOException;

@Slf4j
public class ResponseUtil {
    public static void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String s = ("{ \"message\" : \"" + errorCode.getMessage()
                + "\", \"code\" : \"" +  errorCode.getCode()
                + "\", \"errors\" : [ ] }");
        log.info("{} : {}" , errorCode.getMessage(), s);
        response.getWriter().println(s);
    }
}
