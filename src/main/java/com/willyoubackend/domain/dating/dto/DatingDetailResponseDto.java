package com.willyoubackend.domain.dating.dto;

import com.willyoubackend.domain.dating.entity.Dating;
import lombok.Getter;

import java.util.List;

@Getter
public class DatingDetailResponseDto {
    private final Long datingId;
    private final Long userId;
    private final String datingTitle;
    private final String datingLocation;
    private final String datingTheme;
    private final List<ActivityResponseDto> activityResponseDtoList;

    public DatingDetailResponseDto(Dating dating, List<ActivityResponseDto> activityResponseDtoList) {
        this.datingId = dating.getId();
        this.userId = dating.getUser().getId();
        this.datingTitle = dating.getTitle();
        this.datingLocation = dating.getLocation();
        this.datingTheme = dating.getTheme();
        this.activityResponseDtoList = activityResponseDtoList;
    }
}