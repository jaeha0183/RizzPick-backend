package com.willyoubackend.domain.user_like_match.dto;

import com.willyoubackend.domain.user_profile.dto.ImageResponseDto;
import lombok.Getter;

@Getter
public class LikeStatusResponseDto {
    private final String nickname;
    private final Long userId;
    private final ImageResponseDto profilePic;

    public LikeStatusResponseDto(String nickname, Long userId, ImageResponseDto profilePic) {
        this.nickname = nickname;
        this.userId = userId;
        this.profilePic = profilePic;
    }
}