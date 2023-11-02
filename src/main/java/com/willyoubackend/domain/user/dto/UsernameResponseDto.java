package com.willyoubackend.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UsernameResponseDto {
    private String username;

    public UsernameResponseDto(String username) {
        this.username = username;
    }
}
