package ssamppong.fitchingWeb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ssamppong.fitchingWeb.config.security.PrincipalDetails;

@RestController
@Slf4j
public class oauthController {
    @GetMapping("/hello")
    public ResponseEntity<?> hello(@AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails == null){
            return ResponseEntity.badRequest().build();
        }
        log.info(principalDetails.getUser().toString());
        return ResponseEntity.ok().build();
    }
}
