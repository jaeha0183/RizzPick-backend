package com.willyoubackend.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResetPasswordByEmailRequestDto {
    private String username;
    private String email;

}
