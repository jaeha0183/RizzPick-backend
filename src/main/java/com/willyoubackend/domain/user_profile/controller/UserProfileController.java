package com.willyoubackend.domain.user_profile.controller;

import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.user_profile.dto.UserProfileRequestDto;
import com.willyoubackend.domain.user_profile.service.UserProfileService;
import com.willyoubackend.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/userProfile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    // 회원 프로필 업데이트
    @PutMapping("/updateProfile")
    public ResponseEntity<ApiResponse<String>> updateUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserProfileRequestDto userProfileRequestDto) {
        userProfileService.updateUserProfile(userDetails.getUser(), userProfileRequestDto);
        return ResponseEntity.ok(ApiResponse.successMessage("회원 프로필 업데이트 완료."));
    }
}
