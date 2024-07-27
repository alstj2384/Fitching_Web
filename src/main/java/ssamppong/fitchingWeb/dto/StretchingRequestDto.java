package ssamppong.fitchingWeb.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StretchingRequestDto {

    private Long userId;

    private Long partId;

}
