package ssamppong.fitchingWeb.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
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
    private String providerId;
    private String role;

    public User update(String name){
        this.name = name;

        return this;
    }



}
