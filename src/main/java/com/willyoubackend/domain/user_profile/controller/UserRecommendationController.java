package com.willyoubackend.domain.user_profile.controller;

import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.user_profile.dto.UserProfileResponseDto;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "회원 추천 로직", description = "회원 추천 로직입니다")
@RestController
@Slf4j(topic = "User Recommendations")
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserRecommendationController {
    private final UserRecommendationService userRecommendationService;

    @Operation(summary = "Recommendation create")
    @PostMapping("/recommendation")
    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> createUserRecommendation(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserRecommendationRequestDto requestDto
            ) {
        return userRecommendationService.createRecommendation(userDetails.getUser(), requestDto);
    }

    @Operation(summary = "Recommendation Read")
    @GetMapping("/recommendation")
    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> getUserRecommendation(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return userRecommendationService.getUserRecommendation(userDetails.getUser());
    }

    @Operation(summary = "Recommendation Update")
    @PutMapping("/recommendation")
    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> updateUserRecommendation(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserRecommendationRequestDto requestDto
    ) {
        return userRecommendationService.updateUserRecommendation(userDetails.getUser(), requestDto);
    }

    @Operation(summary = "Recommended Users Test")
    @GetMapping("/recommendation/profile")
    public ResponseEntity<ApiResponse<List<UserProfileResponseDto>>> getRecommendedUsers(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return userRecommendationService.getRecommendedUsers(userDetails.getUser());
    }
}
