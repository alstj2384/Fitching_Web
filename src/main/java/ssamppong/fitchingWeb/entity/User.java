package ssamppong.fitchingWeb.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"bodyParts"})
@ToString(exclude = {"bodyParts"})
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String email;
    private int level;
    private int currentPoints;


    private int completedStretchings; // 완료한 스트레칭 횟수 추가
    private String tier; // 사용자 티어 추가

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference //양방향 매핑된 엔티티들 사이의 직렬화 시 무한 루프 문제를 해결
//    private List<BodyPart> bodyParts;
}
