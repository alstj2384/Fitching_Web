package ssamppong.fitchingWeb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssamppong.fitchingWeb.dto.UserPartsDTO;
import ssamppong.fitchingWeb.service.StretchingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stretching")
public class StretchingController {

    private final StretchingService stretchingService;

    @GetMapping("/parts/{userId}")
    public ResponseEntity<?> getPartsByUser(@PathVariable long userId) {//반환타입 제네릭 와일드카드(다야한 타입의 응답 본문을 반환)
        try {
            UserPartsDTO userParts = stretchingService.getPartsByUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body(userParts);
        } catch (RuntimeException e) {
            //return new ResponseEntity<>(HttpStatus.NOT_FOUND); //단순한 상태 코드만 설정, 본문 x
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID not found: " + userId);
        }
    }
}