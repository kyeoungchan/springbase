package com.charles.study.springbase.config.auth;

import com.charles.study.springbase.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Spring Security 6 버전 이상부터는 꼭 필요하다!
@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정들을 활성화시켜준다.
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // h2-console 화면을 사용하기 위한 disable
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name()) // 권한 관리 대상을 지정하는 옵션. 해당 api는 USER 권한을 가진 사람만 가능하다.
                        .anyRequest().authenticated()) // 설정된 값들 이외의 url들. authenticated()에 의해 인증된(로그인한) 사용자들만 허용한다.
                .logout(logout -> logout.logoutSuccessUrl("/")) // 로그아웃 성공시 가는 주소
                .oauth2Login(oauth2 -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        oauth2.userInfoEndpoint(userInfoEndpointConfig -> // userInfoEndpoint: OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들 담당
                                userInfoEndpointConfig.userService(customOAuth2UserService))); // 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록한다. 리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있다.

        return http.build();

    }
}
