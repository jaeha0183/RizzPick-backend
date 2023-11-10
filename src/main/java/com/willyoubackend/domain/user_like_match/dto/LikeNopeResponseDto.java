package com.willyoubackend.domain.user_like_match.dto;

import lombok.Getter;

@Getter
public class LikeNopeResponseDto {
    private final String sentUserNickName;
    private final String receivedUserNickName;

    public LikeNopeResponseDto(String sentUserNickName, String receivedUserNickName) {
        this.sentUserNickName = sentUserNickName;
        this.receivedUserNickName = receivedUserNickName;
    }
}