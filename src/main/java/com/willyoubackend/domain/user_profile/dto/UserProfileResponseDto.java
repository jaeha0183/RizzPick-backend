package com.willyoubackend.domain.user_profile.dto;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserProfileResponseDto {
    private Long userId;
    private String nickname;
    private Integer age;
    private String education;
    private String gender;
    private String location;
    private String mbti;
    private String religion;
    private List<String> profileImages;

    public UserProfileResponseDto(UserEntity userEntity){
        this.userId = userEntity.getId();
        this.nickname = userEntity.getUserProfileEntity().getNickname();
        this.age = userEntity.getUserProfileEntity().getAge();
        this.education = userEntity.getUserProfileEntity().getEducation();

        if (userEntity.getUserProfileEntity().getGender() != null) {
            this.gender = userEntity.getUserProfileEntity().getGender().name();
        }
        if (userEntity.getUserProfileEntity().getLocation() != null) {
            this.location = userEntity.getUserProfileEntity().getLocation().name();
        }
        if (userEntity.getUserProfileEntity().getMbti() != null) {
            this.mbti = userEntity.getUserProfileEntity().getMbti().name();
        }
        if (userEntity.getUserProfileEntity().getReligion() != null) {
            this.religion = userEntity.getUserProfileEntity().getReligion().name();
        }

        this.profileImages = userEntity.getProfileImages().stream().map(ProfileImageEntity::getImage).collect(Collectors.toList());
    }
}
