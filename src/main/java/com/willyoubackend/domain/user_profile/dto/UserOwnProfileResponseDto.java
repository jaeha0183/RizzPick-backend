package com.willyoubackend.domain.user_profile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.willyoubackend.domain.dating.dto.DatingResponseDto;
import com.willyoubackend.domain.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserOwnProfileResponseDto {
    private Long userId;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String intro;
    private String hobby;
    private String interest;
    private String gender;
    private String location;
    private String mbti;
    private String religion;
    private List<ImageResponseDto> profileImages;
    private List<DatingResponseDto> dating;
    private boolean isNew;
    private boolean userActiveStatus;

    public UserOwnProfileResponseDto(UserEntity userEntity, List<DatingResponseDto> datingResponseDtoList, boolean isNew, boolean userActiveStatus) {
        this.userId = userEntity.getId();
        this.nickname = userEntity.getUserProfileEntity().getNickname();
        this.birthday = userEntity.getUserProfileEntity().getBirthday();
        this.intro = userEntity.getUserProfileEntity().getIntro();
        this.hobby = userEntity.getUserProfileEntity().getHobby();
        this.interest = userEntity.getUserProfileEntity().getInterest();
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
        this.dating = datingResponseDtoList;
        this.isNew = userEntity.getUserProfileEntity().isNew();
        this.userActiveStatus = userEntity.getUserProfileEntity().isUserActiveStatus();
    }
}