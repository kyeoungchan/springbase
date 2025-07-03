package com.charles.study.springbase.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.*;

class ProfileControllerUnitTest {

    @Test
    @DisplayName("real profile 이 조회된다.")
    void realProfileTest() {
        // given
        String expectedProfile = "real";
        MockEnvironment env = new MockEnvironment();

        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("oauth");
        env.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(env);

        // when
        String profile = controller.profile();

        // then
        assertEquals(expectedProfile, profile);
    }

    @Test
    @DisplayName("real profile 이 없으면 첫 번째가 조회된다.")
    void noneRealProfileTest() {
        // given
        String expectedProfile = "oauth";
        MockEnvironment env = new MockEnvironment();

        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(env);

        // when
        String profile = controller.profile();

        // then
        assertEquals(expectedProfile, profile);
    }

    @Test
    @DisplayName("active profile 이 없으면 default 가 조회된다.")
    void noneActiveProfileTest() {
        // given
        String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();
        ProfileController controller = new ProfileController(env);

        // when
        String profile = controller.profile();

        // then
        assertEquals(expectedProfile, profile);
    }
}