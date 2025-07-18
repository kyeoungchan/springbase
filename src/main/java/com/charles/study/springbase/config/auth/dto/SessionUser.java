package com.charles.study.springbase.config.auth.dto;

import com.charles.study.springbase.domain.user.User;
import java.io.Serializable;
import lombok.Getter;

/**
 * 인증된 사용자 정보만 필요하다.
 */
@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
