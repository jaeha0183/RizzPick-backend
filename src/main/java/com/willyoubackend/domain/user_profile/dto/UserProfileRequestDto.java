package com.willyoubackend.domain.user_profile.dto;

import com.willyoubackend.global.validation.KoreanSize;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserProfileRequestDto {
    @KoreanSize(max = 6, message = "닉네임(한글)은 최대 6자 입니다.")
    @Size(max = 8, message = "닉네임은 최대 8자 입니다.")
    private String nickname;
    private int age;
    private String intro;
    private String education;
    private boolean userActiveStatus;
    private String gender;
    private String location;
    private String mbti;
    private String religion;
}