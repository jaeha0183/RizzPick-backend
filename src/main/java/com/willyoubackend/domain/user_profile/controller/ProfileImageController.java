package com.willyoubackend.domain.user_profile.controller;

import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.user_profile.dto.ProfileImageRequestDto;
import com.willyoubackend.domain.user_profile.service.ProfileImageService;
import com.willyoubackend.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/profileImage")
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    @PutMapping(value = "/updateImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ModelAttribute ProfileImageRequestDto profileImageRequestDto,
            @RequestParam(value = "image") MultipartFile image) throws IOException {
        log.info("userId : {}", userDetails.getUser());
        profileImageService.updateProfileImage(userDetails.getUser(), profileImageRequestDto, image);
        return ResponseEntity.ok(ApiResponse.successMessage("프로필 사진 업데이트 완료."));
    }
}
