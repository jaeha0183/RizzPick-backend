package com.willyoubackend.domain.user.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private final Long userId;
    private final Boolean userActiveStatus;

    public LoginResponseDto(Long userId, Boolean userActiveStatus) {
        this.userId = userId;
        this.userActiveStatus = userActiveStatus;
    }
}
