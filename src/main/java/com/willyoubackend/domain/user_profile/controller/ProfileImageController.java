package com.willyoubackend.domain.user_profile.controller;

import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.user_profile.dto.ProfileImageRequestDto;
import com.willyoubackend.domain.user_profile.service.ProfileImageService;
import com.willyoubackend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "프로필 이미지", description = "프로필 이미지")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/profileImage")
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    // 프로필 이미지 설정
    @Operation(summary = "프로필 이미지 설정")
    @PutMapping(value = "/updateImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ModelAttribute ProfileImageRequestDto profileImageRequestDto) throws IOException {
        log.info(profileImageRequestDto.toString());
        profileImageService.updateProfileImage(userDetails.getUser(), profileImageRequestDto);
        return ResponseEntity.ok(ApiResponse.successMessage("프로필 사진 업데이트 완료."));
    }
}
