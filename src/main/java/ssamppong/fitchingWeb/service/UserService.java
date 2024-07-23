package ssamppong.fitchingWeb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssamppong.fitchingWeb.dto.UserDto;
import ssamppong.fitchingWeb.entity.User;
import ssamppong.fitchingWeb.mapper.UserMapper;
import ssamppong.fitchingWeb.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto findById(Long id){
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()){
            User user = byId.get();
            return UserMapper.toDto(user);

        }else {
            throw new RuntimeException("User not found");
        }

    }


}
