package com.willyoubackend.domain.user_profile.dto;

import com.willyoubackend.domain.user_profile.entity.GenderRecommendationEnum;
import lombok.Getter;

@Getter
public class UserRecommendationRequestDto {
    private GenderRecommendationEnum recGender;
    private String selectedGender;
    private Boolean recAge;
    private Long ageGap;
    private Boolean recLocation;
    private Float distance;
    private Float longitude;
    private Float latitude;
}
