package com.willyoubackend.domain.dating.dto;

import com.willyoubackend.domain.dating.entity.Dating;
import com.willyoubackend.domain.user_profile.dto.ImageResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class DatingDetailResponseDto {
    private final Long datingId;
    private final Long userId;
    private final String datingTitle;
    private final String datingLocation;
    private final String datingTheme;
    private final LocalDateTime createdAt;
    private final List<ActivityResponseDto> activityResponseDtoList;
    private final ImageResponseDto datingImage;

    public DatingDetailResponseDto(Dating dating, List<ActivityResponseDto> activityResponseDtoList) {
        this.datingId = dating.getId();
        this.datingImage = (dating.getDatingImage() == null) ? new ImageResponseDto(dating.getUser().getProfileImages().get(0)) : new ImageResponseDto(dating.getDatingImage());
        this.createdAt = dating.getCreatedAt();
        this.userId = dating.getUser().getId();
        this.datingTitle = dating.getTitle();
        this.datingLocation = dating.getLocation();
        this.datingTheme = dating.getTheme();
        this.activityResponseDtoList = activityResponseDtoList;
    }
}