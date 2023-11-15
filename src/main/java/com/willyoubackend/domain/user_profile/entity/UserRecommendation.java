package com.willyoubackend.domain.user_profile.entity;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.dto.UserRecommendationRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 성별
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderRecommendationEnum recGender;

    // 나이
    @Column(nullable = false)
    private Boolean recAge;

    @Column(nullable = true)
    private Long ageGap;

    // 지역
    @Column(nullable = false)
    private Boolean recLocation;
    // Distnace
    @Column(nullable = true)
    private Float distance;
    // 경도
    @Column(nullable = true)
    private Float longitude;
    // 위도
    @Column(nullable = true)
    private Float latitude;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public UserRecommendation(UserRecommendationRequestDto requestDto) {
        this.recGender = requestDto.getRecGender();
        this.recAge = requestDto.getRecAge();
        this.ageGap = requestDto.getAgeGap();
        this.recLocation = requestDto.getRecLocation();
        this.distance = requestDto.getDistance();
        this.longitude = requestDto.getLongitude();
        this.latitude = requestDto.getLatitude();
    }

    public UserRecommendation(GenderRecommendationEnum recGender, Boolean recAge, Long ageGap, Boolean recLocation, Float distance, Float longitude, Float latitude) {
        this.recGender = recGender;
        this.recAge = recAge;
        this.ageGap = ageGap;
        this.recLocation = recLocation;
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void update(UserRecommendationRequestDto requestDto) {
        this.recGender = requestDto.getRecGender();
        this.recAge = requestDto.getRecAge();
        this.ageGap = requestDto.getAgeGap();
        this.recLocation = requestDto.getRecLocation();
        this.distance = requestDto.getDistance();
        this.longitude = requestDto.getLongitude();
        this.latitude = requestDto.getLatitude();
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
