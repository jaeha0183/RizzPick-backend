package com.willyoubackend.domain.user_profile.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.dto.UserRecommendationRequestDto;
import com.willyoubackend.domain.user_profile.dto.UserRecommendationResponseDto;
import com.willyoubackend.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRecommendationService {
    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> createRecommendation(UserEntity user, UserRecommendationRequestDto requestDto) {
        return null;
    }

    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> getUserRecommendation(UserEntity user) {
        return null;
    }

    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> updateUserRecommendation(UserEntity user, UserRecommendationRequestDto requestDto) {
        return null;
    }
}
