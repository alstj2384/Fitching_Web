package ssamppong.fitchingWeb.global.oauth2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ssamppong.fitchingWeb.entity.User;

import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String providerId;
    private String name;
    private String email;

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName, // 회원 구분 PK
                                     Map<String, Object> attributes){
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .providerId((String)attributes.get("sub"))
                .build();
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .role("USER")
                .email(email)
                .providerId(providerId)
                .build();
    }
}

