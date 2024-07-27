package ssamppong.fitchingWeb.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssamppong.fitchingWeb.dto.PartDTO;
import ssamppong.fitchingWeb.dto.StretchingRequestDto;
import ssamppong.fitchingWeb.dto.StretchingResponseDto;
import ssamppong.fitchingWeb.dto.UserPartsDTO;
import ssamppong.fitchingWeb.entity.BodyPart;
import ssamppong.fitchingWeb.entity.User;
import ssamppong.fitchingWeb.repository.BodyPartRepository;
import ssamppong.fitchingWeb.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StretchingService {

    private final UserRepository userRepository;
    private final BodyPartRepository bodyPartRepository;

    public UserPartsDTO getPartsByUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PartDTO> parts = user.getBodyParts().stream()
                .map(part -> PartDTO.builder()
                        .partName(part.getPartName())
                        .count(part.getCount())
                        .build())
                .collect(Collectors.toList());

        return UserPartsDTO.builder()
                .userId(user.getUserId())
                .parts(parts)
                .build();
    }

    @Transactional
    public StretchingResponseDto completeStretching(StretchingRequestDto requestDto){
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(()
                -> new RuntimeException("User not found"));

        BodyPart bodyPart = bodyPartRepository.findById(requestDto.getPartId())
                .orElseThrow(() -> new RuntimeException("BodyPart not found"));
        user.setCurrentPoints(user.getCurrentPoints() + 10); // 예시로 10 포인트 증가
        user.setCompletedStretchings(user.getCompletedStretchings() + 1); // 완료한 스트레칭 횟수 증가

        bodyPart.setCount(user.getCompletedStretchings());

        // 레벨과 티어 업데이트
        updateLevelAndTier(user);

        userRepository.save(user);
        bodyPartRepository.save(bodyPart);

        return new StretchingResponseDto(user.getCurrentPoints(), user.getLevel(), user.getTier(), true);
    }

    private void updateLevelAndTier(User user) {
        int completedStretchings = user.getCompletedStretchings();

        if (completedStretchings >= 50) {
            user.setLevel(6);
            user.setTier("Challenger");
        } else if (completedStretchings >= 25) {
            user.setLevel(5);
            user.setTier("Grand Master");
        } else if (completedStretchings >= 15) {
            user.setLevel(4);
            user.setTier("Diamond");
        } else if (completedStretchings >= 10) {
            user.setLevel(3);
            user.setTier("Platinum");
        } else if (completedStretchings >= 8) {
            user.setLevel(2);
            user.setTier("Gold");
        } else if (completedStretchings >= 5) {
            user.setLevel(1);
            user.setTier("Sliver");
        } else if (completedStretchings >= 3) {
            user.setLevel(0);
            user.setTier("Bronze");
        }
    }
}

