package ssamppong.fitchingWeb.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
@Table(name = "body_parts")
public class BodyPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int partId;
    private String partName;
    private int count;

    @ManyToOne(fetch = FetchType.LAZY) //필요할 때만 연관된 엔티티를 로딩
    @JoinColumn(name = "user_id")
    @JsonBackReference //양뱡향 관계 무한루프 해결
    private User user;
}
