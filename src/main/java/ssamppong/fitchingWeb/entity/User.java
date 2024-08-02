package ssamppong.fitchingWeb.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;


import java.util.*;

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

    @Column(unique = true)
    private String email;
    private String name;
    private String role;

    // level
    @Column(name = "member_level")
    private int level;
    private int currentPoints;

    private int completedStretchings; // 완료한 스트레칭 횟수 추가
    private String tier; // 사용자 티어 추가

    // 일반 로그인
    private String password;

    // oauth
    private String providerId;

    // jwt
    private String refreshToken;

    public User update(String name){
        this.name = name;

        return this;
    }

    public void passwordEncode(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken){
        this.refreshToken = updateRefreshToken;
    }


}
