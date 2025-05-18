package com.charles.study.springbase.web.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloResponseDtoTest {

    @Test
    public void lombokTest() {
        // given
        String name = "test";
        int amount = 1000;

        // when
        HelloResponseDto helloResponseDto = new HelloResponseDto(name, amount);

        // then
        assertEquals(name, helloResponseDto.getName());
        assertEquals(amount, helloResponseDto.getAmount());
    }

}