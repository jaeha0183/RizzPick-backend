package com.willyoubackend.domain.sse.dto;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.dto.ImageResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserDto {
    private Long id;
//    private List<ImageResponseDto> profileImages;

    public UserDto(UserEntity user) {
        this.id = user.getId();
//        this.profileImages = user.getProfileImages().stream().map(ImageResponseDto::new).collect(Collectors.toList());
    }
}
