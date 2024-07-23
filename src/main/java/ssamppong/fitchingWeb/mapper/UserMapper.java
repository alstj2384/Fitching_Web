package ssamppong.fitchingWeb.mapper;


import ssamppong.fitchingWeb.dto.UserDto;
import ssamppong.fitchingWeb.entity.User;

public class UserMapper {

    //user클래스의 데이터를 Dto로 매핑
    public static UserDto toDto(User user){
        return UserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .level(user.getLevel())
                .currentPoints(user.getCurrentPoints())
                .build();
    }

}
