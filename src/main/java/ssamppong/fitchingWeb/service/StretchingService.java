package ssamppong.fitchingWeb.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssamppong.fitchingWeb.dto.PartDTO;
import ssamppong.fitchingWeb.dto.UserPartsDTO;
import ssamppong.fitchingWeb.entity.User;
import ssamppong.fitchingWeb.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StretchingService {

    private final UserRepository userRepository;

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
}
