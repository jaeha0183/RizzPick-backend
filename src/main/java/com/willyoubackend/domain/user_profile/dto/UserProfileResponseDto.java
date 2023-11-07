package com.willyoubackend.domain.user_profile.dto;

import com.willyoubackend.domain.dating.dto.DatingResponseDto;
import com.willyoubackend.domain.user.entity.UserEntity;
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
    private String intro;
    private String education;
    private String gender;
    private String location;
    private String mbti;
    private String religion;
    private List<ImageResponseDto> profileImages;
    private DatingResponseDto dating;
//    private boolean isNew;
//    private boolean userActiveStatus;

    public UserProfileResponseDto(UserEntity userEntity) {
        this.userId = userEntity.getId();
        this.nickname = userEntity.getUserProfileEntity().getNickname();
        this.age = userEntity.getUserProfileEntity().getAge();
        this.intro = userEntity.getUserProfileEntity().getIntro();
        this.education = userEntity.getUserProfileEntity().getEducation();
        this.location = userEntity.getUserProfileEntity().getLocation();
        this.mbti = userEntity.getUserProfileEntity().getMbti();
        this.religion = userEntity.getUserProfileEntity().getReligion();

        if (userEntity.getUserProfileEntity().getGender() != null) {
            this.gender = userEntity.getUserProfileEntity().getGender().name();
        }
//        if (userEntity.getUserProfileEntity().getLocation() != null) {
//            this.location = userEntity.getUserProfileEntity().getLocation().getThemeName();
//        }
//        if (userEntity.getUserProfileEntity().getMbti() != null) {
//            this.mbti = userEntity.getUserProfileEntity().getMbti().name();
//        }
//        if (userEntity.getUserProfileEntity().getReligion() != null) {
//            this.religion = userEntity.getUserProfileEntity().getReligion().getThemeName();
//        }

        this.profileImages = userEntity.getProfileImages().stream().map(ImageResponseDto::new).collect(Collectors.toList());
        this.dating = (userEntity.getUserProfileEntity().getDating() != null) ? new DatingResponseDto(userEntity.getUserProfileEntity().getDating()) : null;
//        this.isNew = userEntity.getUserProfileEntity().isNew();
//        this.userActiveStatus = userEntity.getUserProfileEntity().isUserActiveStatus();
    }
}