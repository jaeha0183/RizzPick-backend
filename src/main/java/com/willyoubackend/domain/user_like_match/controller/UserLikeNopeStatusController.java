package com.willyoubackend.domain.user_like_match.controller;

import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.user_like_match.dto.LikeStatusResponseDto;
import com.willyoubackend.domain.user_like_match.service.UserLikeStatusService;
import com.willyoubackend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "유저의 좋아요 현황", description = "유저가 받은 그리고 보낸 좋아요를 확힌 할 수 있습니다.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "User Like Status Controller")
public class UserLikeNopeStatusController {
    private final UserLikeStatusService userLikeStatusService;

    @Operation(summary = "유조가 보낸 좋아요", description = "유저가 다른 유저에게 보낸 좋아요를 조회 할 수 있습니다.")
    // 사용자가 보낸 좋아요를 조회한다.
    @GetMapping("/like/status")
    public ResponseEntity<ApiResponse<List<LikeStatusResponseDto>>> getUserLikeStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return userLikeStatusService.getUserLikeStatus(userDetails.getUser());
    }

    // 사용자가 받은 좋아요를 조회한다.
    @Operation(summary = "유저가 받은 좋아요", description = "유저가 다른 유저에게 받은 좋아룔 조회 할 수 있습니다.")
    @GetMapping("/likedby/status")
    public ResponseEntity<ApiResponse<List<LikeStatusResponseDto>>> getUserLikedByStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return userLikeStatusService.getUserLikedByStatus(userDetails.getUser());
    }
}
