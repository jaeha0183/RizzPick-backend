package com.willyoubackend.domain.user_profile.dto;

import com.willyoubackend.domain.dating.dto.DatingResponseDto;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_like_match.dto.MatchResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserProfileMatchResponseDto {
    private Long userId;
    private String nickname;
    private LocalDate birthday;
    private String intro;
    private String education;
    private String gender;
    private String location;
    private String mbti;
    private String religion;
    private List<ImageResponseDto> profileImages;
    private DatingResponseDto dating;
    private Long matchId;

    public UserProfileMatchResponseDto(UserEntity userEntity, Long matchId) {
        this.userId = userEntity.getId();
        this.nickname = userEntity.getUserProfileEntity().getNickname();
        this.birthday = userEntity.getUserProfileEntity().getBirthday();
        this.intro = userEntity.getUserProfileEntity().getIntro();
        this.education = userEntity.getUserProfileEntity().getEducation();
        this.location = userEntity.getUserProfileEntity().getLocation();
        this.mbti = userEntity.getUserProfileEntity().getMbti();
        this.religion = userEntity.getUserProfileEntity().getReligion();
        this.matchId = matchId;

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
    }
}