package com.willyoubackend.domain.dating.dto;

import com.willyoubackend.domain.dating.entity.Dating;
import com.willyoubackend.domain.user_profile.dto.ImageResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DatingResponseDto {
    private final Long datingId;
    private final Long userId;
    private final String datingTitle;
    private final String datingLocation;
    private final String datingTheme;
    private final LocalDateTime createdAt;
    private final String userNickname;
    private final ImageResponseDto datingImage;

    public DatingResponseDto(Dating dating) {
        this.datingId = dating.getId();
        this.userId = dating.getUser().getId();
        this.datingImage = (dating.getDatingImage() == null) ? new ImageResponseDto(dating.getUser().getProfileImages().get(0)) : new ImageResponseDto(dating.getDatingImage());
        this.userNickname = dating.getUser().getUserProfileEntity().getNickname();
        this.datingTitle = dating.getTitle();
        this.datingLocation = dating.getLocation();
        this.datingTheme = dating.getTheme();
        this.createdAt = dating.getCreatedAt();
    }
}