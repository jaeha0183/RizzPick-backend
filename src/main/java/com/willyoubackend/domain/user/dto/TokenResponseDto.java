package com.willyoubackend.domain.user.dto;

import lombok.Getter;

@Getter
public class TokenResponseDto {
    private final String newAccessToken;

    public TokenResponseDto(String newAccessToken) {
        this.newAccessToken = newAccessToken;
    }
}




