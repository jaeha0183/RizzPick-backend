package com.willyoubackend.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestDto {
    public String refreshToken;
}