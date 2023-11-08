package com.willyoubackend.domain.user_profile.dto;

import com.willyoubackend.domain.dating.dto.DatingResponseDto;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.service.UserProfileService;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserProfileResponseDto {
    private Long userId;
    private String nickname;
    private String birthday;
    private String intro;
    private String education;
    private String gender;
    private String location;
    private String mbti;
    private String religion;
    private List<ImageResponseDto> profileImages;
    private DatingResponseDto dating;

    public UserProfileResponseDto(UserEntity userEntity) {
        this.userId = userEntity.getId();
        this.nickname = userEntity.getUserProfileEntity().getNickname();
        this.birthday = userEntity.getUserProfileEntity().getBirthday();
//        if(age<19){
//            throw new CustomException(ErrorCode.INVALID_AGE);
//        }
        this.intro = userEntity.getUserProfileEntity().getIntro();
        this.education = userEntity.getUserProfileEntity().getEducation();

        if (userEntity.getUserProfileEntity().getGender() != null) {
            this.gender = userEntity.getUserProfileEntity().getGender().name();
        }
        if (userEntity.getUserProfileEntity().getLocation() != null) {
            this.location = userEntity.getUserProfileEntity().getLocation().getThemeName();
        }
        if (userEntity.getUserProfileEntity().getMbti() != null) {
            this.mbti = userEntity.getUserProfileEntity().getMbti().name();
        }
        if (userEntity.getUserProfileEntity().getReligion() != null) {
            this.religion = userEntity.getUserProfileEntity().getReligion().getThemeName();
        }

        this.profileImages = userEntity.getProfileImages().stream().map(ImageResponseDto::new).collect(Collectors.toList());
        this.dating = (userEntity.getUserProfileEntity().getDating() != null) ? new DatingResponseDto(userEntity.getUserProfileEntity().getDating()) : null;
    }
}