package ssamppong.fitchingWeb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssamppong.fitchingWeb.dto.UserSignUpDto;
import ssamppong.fitchingWeb.service.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception{
        userService.signUp(userSignUpDto);
        return "회원가입 성공";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> nullex(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
