package ssamppong.fitchingWeb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssamppong.fitchingWeb.dto.UserDto;
import ssamppong.fitchingWeb.entity.User;
import ssamppong.fitchingWeb.mapper.UserMapper;
import ssamppong.fitchingWeb.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ssamppong.fitchingWeb.dto.UserSignUpDto;
import java.util.Optional;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserSignUpDto userSignUpDto) throws Exception {
        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다");
        }

        User user = User.builder()
                .email(userSignUpDto.getEmail())
                .password(userSignUpDto.getPassword())
                .name(userSignUpDto.getName())
                .role("USER")
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);

    }

    public UserDto findById(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            return UserMapper.toDto(user);

        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User findByEmail(String email) {
        Optional<User> byId = userRepository.findByEmail(email);
        if (byId.isPresent()) {
            User user = byId.get();
            return user;

        } else {
            throw new RuntimeException("User not found");
        }
    }
}
