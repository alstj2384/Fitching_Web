package ssamppong.fitchingWeb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssamppong.fitchingWeb.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssamppong.fitchingWeb.dto.UserSignUpDto;
import ssamppong.fitchingWeb.entity.User;
import ssamppong.fitchingWeb.service.UserService;


@Slf4j@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception{
        userService.signUp(userSignUpDto);
        return "회원가입 성공";
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("user_id") int id) {
        try {
            User user = userService.findByEmail(userDetails.getUsername());
            if(user.getUserId() != id){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청 정보가 일치하지 않습니다: " + id);
            }
            UserDto userDto = userService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID not found: " + id);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> nullex(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());

    }
}
