package ssamppong.fitchingWeb.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private int userId;

    private String email;

    private int level;

    private int currentPoints;

    private String tiar;


}
