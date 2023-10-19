package com.willyoubackend.domain.user_like_match.dto;

import com.willyoubackend.domain.user_profile.dto.UserProfileResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class LikeAlertResponseDto {
    private final Integer likeCount;
    private final List<UserProfileResponseDto> userProfileResponseDtoList;

    public LikeAlertResponseDto(Integer likeCount, List<UserProfileResponseDto> userProfileResponseDtoList) {
        this.likeCount = likeCount;
        this.userProfileResponseDtoList = userProfileResponseDtoList;
    }
}