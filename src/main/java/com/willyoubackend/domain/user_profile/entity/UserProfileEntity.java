package com.willyoubackend.domain.user_profile.entity;

import com.willyoubackend.domain.dating.entity.Dating;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.dto.UserProfileRequestDto;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String nickname;

    @Column(nullable = true)
    private Integer age;

    @Column(nullable = true)
    private String intro;

    @Column(nullable = true)
    private String education;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean userActiveStatus;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isNew = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private GenderEnum gender;

    @Column(nullable = true)
    private String location;

    @Column(nullable = true)
    private String mbti;

    @Column(nullable = true)
    private String religion;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToOne
    @JoinColumn(name = "user_dating_id")
    private Dating dating;

    public void updateProfile(UserProfileRequestDto userProfileRequestDto) {
        this.nickname = userProfileRequestDto.getNickname();
        this.age = userProfileRequestDto.getAge();
        this.intro = userProfileRequestDto.getIntro();
        this.education = userProfileRequestDto.getEducation();
        this.userActiveStatus = userProfileRequestDto.isUserActiveStatus();
        this.location = userProfileRequestDto.getLocation();
        this.mbti = userProfileRequestDto.getMbti();
        this.religion = userProfileRequestDto.getReligion();

        try {
            if (userProfileRequestDto.getGender() != null) {
                this.gender = GenderEnum.valueOf(userProfileRequestDto.getGender());
            }
//            if (userProfileRequestDto.getLocation() != null) {
//                this.location = LocationEnum.findByThemeName(userProfileRequestDto.getLocation());
//            }
//            if (userProfileRequestDto.getMbti() != null) {
//                this.mbti = MbtiEnum.valueOf(userProfileRequestDto.getMbti());
//            }
//            if (userProfileRequestDto.getReligion() != null) {
//                this.religion = ReligionEnum.findByThemeName(userProfileRequestDto.getReligion());
//            }
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VAL);
        }
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public void setDating(Dating dating) {
        this.dating = dating;
    }

    public void setUserActiveStatus(boolean userActiveStatus) {
        this.userActiveStatus = userActiveStatus;
        if (userActiveStatus) {
            this.isNew = false;
        }
    }

    public boolean isNew() {
        return isNew;
    }
}