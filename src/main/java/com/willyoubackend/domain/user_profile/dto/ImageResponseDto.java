package com.willyoubackend.domain.user_profile.dto;

import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageResponseDto {
    private Long id;
    private String image;

    public ImageResponseDto(ProfileImageEntity profileImageEntity) {
        this.id = profileImageEntity.getId();
        this.image = profileImageEntity.getImage();
    }
}