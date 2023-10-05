package com.willyoubackend.domain.user_profile.dto;

import lombok.Getter;

@Getter
public class UserProfileRequestDto {
    private String nickname;
    private int age;
    private String education;
    private boolean userActiveStatus;
    private String gender;
    private String location;
    private String mbti;
    private String religion;
}
