package com.charles.study.springbase.config.auth;

import com.charles.study.springbase.config.auth.dto.OAuthAttributes;
import com.charles.study.springbase.config.auth.dto.SessionUser;
import com.charles.study.springbase.domain.user.User;
import com.charles.study.springbase.domain.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /*현재 로그인 진행 중인 서비스를 구분하는 코드다.
        * 지금은 구글만 사용하는 불필요한 값이지만, 이후 네이버 로그인 연동 시에 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용한다.*/
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        /*OAuth2 로그인 진행시 키가 되는 필드값이다. 일종의 PK
        * 구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오 등은 기본 지원하지 않는다.
        * 구글의 기본 코드: "sub"
        * 이후 네이버 로그인과 구글 로그인을 동시에 지원할 때 사용된다.*/
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        /*OAuth2UserService 를 통해 가져온 OAuth2User 의 attribute 를 담은 클래스다.
        * 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용한다.*/
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        log.info("현재 로그인 유저 권한: {}", user.getRoleKey());

        // 세션에 사용자 정보를 저장하기 위한 DTO 클래스다.
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
