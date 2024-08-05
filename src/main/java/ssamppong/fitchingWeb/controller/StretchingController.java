package ssamppong.fitchingWeb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ssamppong.fitchingWeb.dto.StretchingRequestDto;
import ssamppong.fitchingWeb.dto.StretchingResponseDto;
import ssamppong.fitchingWeb.dto.UserPartsDTO;
import ssamppong.fitchingWeb.entity.User;
import ssamppong.fitchingWeb.service.StretchingService;
import ssamppong.fitchingWeb.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stretching")
public class StretchingController {

    private final StretchingService stretchingService;
    private final UserService userService;

//    @GetMapping("/parts/{userId}")
//    public ResponseEntity<?> getPartsByUser(@PathVariable int userId) {//반환타입 제네릭 와일드카드(다야한 타입의 응답 본문을 반환)
//        try {
//            UserPartsDTO userParts = stretchingService.getPartsByUser(userId);
//            return ResponseEntity.status(HttpStatus.OK).body(userParts);
//        } catch (RuntimeException e) {
//            //return new ResponseEntity<>(HttpStatus.NOT_FOUND); //단순한 상태 코드만 설정, 본문 x
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID not found: " + userId);
//        }
//    }
    @GetMapping("/parts/{userId}")
    public ResponseEntity<?> getPartsByUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int userId) {//반환타입 제네릭 와일드카드(다야한 타입의 응답 본문을 반환)
        try {
            User findUser = userService.findByEmail(userDetails.getUsername());

            if(findUser.getUserId() != userId){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청 정보가 일치하지 않습니다: " + userId);
            }
            UserPartsDTO userParts = stretchingService.getPartsByUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body(userParts);
        } catch (RuntimeException e) {
            //return new ResponseEntity<>(HttpStatus.NOT_FOUND); //단순한 상태 코드만 설정, 본문 x
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID not found: " + userId);
        }
    }

    @PostMapping("/check")
    public ResponseEntity<?> stretchingCheck(@RequestBody StretchingRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails,
                                             BindingResult result){

        User findUser = userService.findByEmail(userDetails.getUsername());

        if(findUser.getUserId() != requestDto.getUserId()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청 정보가 일치하지 않습니다: " + findUser.getUserId());
        }
        if (result.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        try {
            StretchingResponseDto responseDto = stretchingService.completeStretching(requestDto);
            return ResponseEntity.ok(responseDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("에러");
        }


    }

}