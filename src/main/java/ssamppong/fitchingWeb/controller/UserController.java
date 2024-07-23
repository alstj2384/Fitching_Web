package ssamppong.fitchingWeb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssamppong.fitchingWeb.dto.UserDto;
import ssamppong.fitchingWeb.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUser(@PathVariable("user_id") Long id){
        try {
            UserDto userDto = userService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID not found: " + id);
        }

    }

}
