package com.willyoubackend.domain.user_profile.dto;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.GenderRecommendationEnum;
import com.willyoubackend.domain.user_profile.entity.UserRecommendation;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
public class UserRecommendationResponseDto {
    private final Long recommendationId;
    private final Long userId;
    private final GenderRecommendationEnum recGender;
    private final Boolean recAge;
    private final Long ageGap;
    private final Boolean recLocation;
    private final Float distance;
    private final Float longitude;
    private final Float latitude;

    public UserRecommendationResponseDto(UserRecommendation userRecommendation) {
        this.recommendationId = userRecommendation.getId();
        this.userId = userRecommendation.getUserEntity().getId();
        this.recGender = userRecommendation.getRecGender();
        this.recAge = userRecommendation.getRecAge();
        this.ageGap = userRecommendation.getAgeGap();
        this.recLocation = userRecommendation.getRecLocation();
        this.distance = userRecommendation.getDistance();
        this.longitude = userRecommendation.getLongitude();
        this.latitude = userRecommendation.getLatitude();
    }
}
