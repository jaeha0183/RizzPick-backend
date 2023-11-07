package com.willyoubackend.domain.user_profile.dto;

import lombok.Getter;

@Getter
public class UserRecommendationRequestDto {
    private Boolean recGender;
    private String selectedGender;
    private Boolean recAge;
    private Long ageGap;
    private Boolean recLocation;
    private Float longitude;
    private Float latitude;
}
