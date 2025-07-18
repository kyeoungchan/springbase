package com.charles.study.springbase.config.auth.dto;

import com.charles.study.springbase.domain.user.Role;
import com.charles.study.springbase.domain.user.User;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    /**
     * OAuth2User 에서 반환하는 사용자 정보는 Map 이기 때문에 값 하나하나를 변환해야만 한다.
     * registrationId: 구글에서는 불필요
     */
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    /**
     * User 엔티티를 생성한다.
     * 가입할 때의 기본 권한을 GUEST 로 주기 위해서 role 빌더 값에는 Role.GUEST 를 사용한다.
     * OAuthAttributes 클래스 생성이 끝났으면 같은 패키지에 SessionUser 클래스를 생성한다.
     */
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * OAuthAttributes 에서 엔티티를 생성하는 시점은 처음 가입할 때이다.
     */
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
