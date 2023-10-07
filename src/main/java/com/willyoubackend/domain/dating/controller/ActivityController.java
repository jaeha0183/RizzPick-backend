package com.willyoubackend.domain.dating.controller;

import com.willyoubackend.domain.dating.dto.ActivityRequestDto;
import com.willyoubackend.domain.dating.dto.ActivityResponseDto;
import com.willyoubackend.domain.dating.service.ActivityService;
import com.willyoubackend.domain.user.security.UserDetailsImpl;
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
@Slf4j(topic = "Activity Controller")
public class ActivityController {
    private final ActivityService activityService;

    //    @PostMapping("/activity")
//    public ResponseEntity<ApiResponse<ActivityResponseDto>> createActivity(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return null;
//    }
    // Create
    // 데이트 활동 생성
    @PostMapping("/activity")
    public ResponseEntity<ApiResponse<ActivityResponseDto>> createActivity(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ActivityRequestDto requestDto
    ) {
        return activityService.activityCreate(userDetails.getUser(), requestDto);
    }

    // Read
    // 전체 데이트 활동 조회
    @GetMapping("/activities")
    public ResponseEntity<ApiResponse<List<ActivityResponseDto>>> getActivityList() {
        return activityService.getActivityList();
    }

    // 로그인한 사용자가 작성한 데이트 활동 조회
    @GetMapping("/activities/user")
    public ResponseEntity<ApiResponse<List<ActivityResponseDto>>> getActivityListByUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return activityService.getActivityListByUser(userDetails.getUser());
    }

    // Update
    // 데이트 활동 수정
    @PutMapping("/activity/{id}")
    public ResponseEntity<ApiResponse<ActivityResponseDto>> updateDating(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody ActivityRequestDto requestDto) {
        return activityService.updateDating(userDetails.getUser(), id, requestDto);
    }
    // Delete
    // 데이트 활동 삭제
    @DeleteMapping("/activity/{id}")
    public ResponseEntity<ApiResponse<ActivityResponseDto>> deleteDating(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        return activityService.deleteDating(userDetails.getUser(), id);
    }
}

