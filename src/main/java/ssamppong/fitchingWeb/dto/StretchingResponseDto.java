package ssamppong.fitchingWeb.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StretchingResponseDto {
    private int currentPoints; // 총 포인트
    private int level; // 현재 레벨
    private String tier; // 현재 티어
    private boolean completed; // 스트레칭 완료 여부
}
