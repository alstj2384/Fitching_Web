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
//@EqualsAndHashCode(exclude = {"bodyParts"})
//@ToString(exclude = {"bodyParts"})
@Table(name = "member")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(unique = true)
    private String email;
    private String name;
    private int level;
    private int currentPoints;
    private String role;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference //양방향 매핑된 엔티티들 사이의 직렬화 시 무한 루프 문제를 해결
//    private List<BodyPart> bodyParts;

    public User update(String name){
        this.name = name;

        return this;
    }
}
