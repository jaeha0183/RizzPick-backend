package com.willyoubackend.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifiRequest {
    @Email
    @NotBlank(message = "이메일(필수)")
    private String email;

    @NotBlank(message = "인증 번호(필수)")
    private String authKey;

    public String getAuthKey() {
        return authKey;
    }
}