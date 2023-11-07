package com.willyoubackend.domain.user_profile.service;

import com.willyoubackend.domain.dating.dto.DatingResponseDto;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.dto.UserRecommendationRequestDto;
import com.willyoubackend.domain.user_profile.dto.UserRecommendationResponseDto;
import com.willyoubackend.domain.user_profile.entity.UserRecommendation;
import com.willyoubackend.domain.user_profile.repository.UserRecommendationRepository;
import com.willyoubackend.global.dto.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRecommendationService {
    private final UserRecommendationRepository userRecommendationRepository;
    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> createRecommendation(UserEntity user, UserRecommendationRequestDto requestDto) {
        UserRecommendation userRecommendation = new UserRecommendation(requestDto);
        userRecommendation.setUserEntity(user);
        UserRecommendationResponseDto responseDto = new UserRecommendationResponseDto(userRecommendationRepository.save(userRecommendation));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successData(responseDto));
    }

    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> getUserRecommendation(UserEntity user) {
        UserRecommendation userRecommendation = userRecommendationRepository.findByUserEntity(user);
        UserRecommendationResponseDto responseDto = new UserRecommendationResponseDto(userRecommendation);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(responseDto));
    }

    @Transactional
    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> updateUserRecommendation(UserEntity user, UserRecommendationRequestDto requestDto) {
        UserRecommendation userRecommendation = userRecommendationRepository.findByUserEntity(user);
        userRecommendation.update(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new UserRecommendationResponseDto(userRecommendation)));
    }
}
