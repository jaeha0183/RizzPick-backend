package com.willyoubackend.domain.user_like_match.controller;

import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.user_like_match.dto.MatchResponseDto;
import com.willyoubackend.domain.user_like_match.service.UserMatchStatusService;
import com.willyoubackend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "유저의 매치 현황", description = "유저가 현재까지 매치된 다른 유저의 상황을 조회 및 매치 취소를 할 수 있습니다.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "User Match Status Controller")
public class UserMatchStatusController {
    private final UserMatchStatusService userMatchStatusService;

    @Operation(summary = "유저의 현재 매치 상황", description = "유저의 현재 매치 상황을 조회 할 수 있습니다.")
    @GetMapping("/matches")
    public ResponseEntity<ApiResponse<List<MatchResponseDto>>> getMatches(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return userMatchStatusService.getMatches(userDetails.getUser());
    }

    @Operation(summary = "매치 취소", description = "매치를 취소 할 수 있습니다.")
    @DeleteMapping("/match/{matchId}")
    public ResponseEntity<ApiResponse<MatchResponseDto>> deleteMatch(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long matchId
    ) {
        return userMatchStatusService.deleteMatch(userDetails.getUser(), matchId);
    }
}