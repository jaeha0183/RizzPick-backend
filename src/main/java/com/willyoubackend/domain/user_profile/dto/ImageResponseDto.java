package com.willyoubackend.domain.user_profile.dto;

import com.willyoubackend.domain.dating.entity.DatingImage;
import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import lombok.Getter;

@Getter
public class ImageResponseDto {
    private Long id;
    private String image;

    public ImageResponseDto(ProfileImageEntity profileImageEntity) {
        this.id = profileImageEntity.getId();
        this.image = profileImageEntity.getImage();
    }

    public ImageResponseDto(DatingImage datingImage) {
        this.id = datingImage.getId();
        this.image = datingImage.getImage();
    }
}