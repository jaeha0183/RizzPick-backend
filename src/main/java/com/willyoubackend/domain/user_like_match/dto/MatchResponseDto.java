package com.willyoubackend.domain.user_like_match.dto;

import lombok.Getter;

@Getter
public class MatchResponseDto {
    private final String matchedUserOneName;
    private final String matchedUserTwoName;

    public MatchResponseDto(String matchedUserOneName, String matchedUserTwoName) {
        this.matchedUserOneName = matchedUserOneName;
        this.matchedUserTwoName = matchedUserTwoName;
    }
}
