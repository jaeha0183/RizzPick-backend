package com.willyoubackend.domain.user_like_match.controller;

import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.user_like_match.dto.MatchResponseDto;
import com.willyoubackend.domain.user_like_match.service.UserMatchStatusService;
import com.willyoubackend.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "User Match Status Controller")
public class UserMatchStatusController {
    private final UserMatchStatusService userMatchStatusService;
    // 사용자와 매치된 사람을 조회한다.
    @GetMapping("/matches")
    public ResponseEntity<ApiResponse<List<MatchResponseDto>>> getMatches(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        return userMatchStatusService.getMatches(userDetails.getUser());
    }
    // 사용자가 매치를 취소한다.
    @DeleteMapping("/match/{matchId}")
    public ResponseEntity<ApiResponse<MatchResponseDto>> deleteMatch(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long matchId
    ) {
        return userMatchStatusService.deleteMatch(userDetails.getUser(), matchId);
    }
}
