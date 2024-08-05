package ssamppong.fitchingWeb.controller;

import jakarta.servlet.http.HttpServletResponse;
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
import ssamppong.fitchingWeb.dto.UserResponseDto;
import ssamppong.fitchingWeb.dto.UserSignUpDto;
import ssamppong.fitchingWeb.entity.User;
import ssamppong.fitchingWeb.global.ResponseUtil;
import ssamppong.fitchingWeb.global.jwt.service.JwtService;
import ssamppong.fitchingWeb.service.UserService;

import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ResponseUtil responseUtil;
    private final JwtService jwtService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserSignUpDto userSignUpDto, HttpServletResponse response) throws Exception{
        User user = userService.signUp(userSignUpDto);

        UserResponseDto userResponseDto = new UserResponseDto(user.getUserId(), user.getName());


        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(user.getEmail(), refreshToken);

        user.updateRefreshToken(refreshToken);
        userService.save(user);

        // 객체 반환
        responseUtil.setSuccessResponse(response, userResponseDto);

//        return ResponseEntity.ok(userResponseDto);
        return null;
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
