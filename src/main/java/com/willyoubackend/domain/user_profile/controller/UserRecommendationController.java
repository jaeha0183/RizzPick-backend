package com.willyoubackend.domain.user_profile.controller;

import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.user_profile.dto.UserRecommendationRequestDto;
import com.willyoubackend.domain.user_profile.dto.UserRecommendationResponseDto;
import com.willyoubackend.domain.user_profile.service.UserRecommendationService;
import com.willyoubackend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 추천 로직", description = "회원 추천 로직입니다")
@RestController
@Slf4j(topic = "User Recommendations")
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserRecommendationController {
    private final UserRecommendationService UserRecommendationService;

    @Operation(summary = "Recommendation create")
    @PostMapping("/recommendation/create")
    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> createUserRecommendation(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserRecommendationRequestDto requestDto
            ) {
        return UserRecommendationService.createRecommendation(userDetails.getUser(), requestDto);
    }

    @Operation(summary = "Recommendation Read")
    @PostMapping("/recommendation/create")
    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> getUserRecommendation(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return UserRecommendationService.getUserRecommendation(userDetails.getUser());
    }

    @Operation(summary = "Recommendation Update")
    @PostMapping("/recommendation/create")
    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> updateUserRecommendation(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserRecommendationRequestDto requestDto
    ) {
        return UserRecommendationService.updateUserRecommendation(userDetails.getUser(), requestDto);
    }

}
