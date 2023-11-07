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
    @Column(nullable = false)
    private Boolean recGender;

    private String selectedGender;

    // 나이
    @Column(nullable = false)
    private Boolean recAge;

    @Column(nullable = true)
    private Long ageGap;

    // 지역
    @Column(nullable = false)
    private Boolean recLocation;
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

    }

    public void update(UserRecommendationRequestDto requestDto) {

    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
