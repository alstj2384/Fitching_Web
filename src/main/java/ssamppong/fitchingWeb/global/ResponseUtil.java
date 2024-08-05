package ssamppong.fitchingWeb.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ssamppong.fitchingWeb.dto.UserResponseDto;
import ssamppong.fitchingWeb.global.jwt.ErrorCode;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class ResponseUtil {
    private final ObjectMapper objectMapper;

    public void setFailureResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String s = ("{ \"message\" : \"" + errorCode.getMessage()
                + "\", \"code\" : \"" +  errorCode.getCode()
                + "\", \"errors\" : [ ] }");
        log.info("{} : {}" , errorCode.getMessage(), s);
        response.getWriter().println(s);
    }

    public void setSuccessResponse(HttpServletResponse response, UserResponseDto userResponseDto) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String message = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userResponseDto);
        response.getWriter().println(message);
    }
}
