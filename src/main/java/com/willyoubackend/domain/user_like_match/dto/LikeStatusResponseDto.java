package com.willyoubackend.domain.user_like_match.dto;

import lombok.Getter;

@Getter
public class LikeStatusResponseDto {
    private final String username;

    public LikeStatusResponseDto(String username) {
        this.username = username;
    }
}