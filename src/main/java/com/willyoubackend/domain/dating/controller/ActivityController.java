package com.willyoubackend.domain.dating.controller;

import com.willyoubackend.domain.dating.dto.ActivityRequestDto;
import com.willyoubackend.domain.dating.dto.ActivityResponseDto;
import com.willyoubackend.domain.dating.entity.Activity;
import com.willyoubackend.domain.dating.repository.ActivityRepository;
import com.willyoubackend.domain.dating.service.ActivityService;
import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.global.dto.ApiResponse;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "데이트 활동 추가", description = "데이트에 활동 CRUD 할 수 있는 API입니다.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "Activity Controller")
public class ActivityController {
    private final ActivityService activityService;
    @Operation(summary = "활동 추가", description = "선택한 데이트를 위한 활동 추가")
    @PostMapping("/activity/{datingId}")
    public ResponseEntity<ApiResponse<ActivityResponseDto>> createActivity(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ActivityRequestDto requestDto,
            @PathVariable Long datingId
    ) {
        return activityService.activityCreate(userDetails.getUser(), requestDto, datingId);
    }

    @Operation(summary = "활동 전체 조회", description = "작성된 모든 활동들을 조회합니다.")
    @GetMapping("/activities")
    public ResponseEntity<ApiResponse<List<ActivityResponseDto>>> getActivityList() {
        return activityService.getActivityList();
    }

    @Operation(summary = "유저가 작성한 활동 조회", description = "로그인한 유저가 자신이 작성한 활동들을 조회할 수 있습니다.")
    @GetMapping("/activities/user")
    public ResponseEntity<ApiResponse<List<ActivityResponseDto>>> getActivityListByUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return activityService.getActivityListByUser(userDetails.getUser());
    }

    @Operation(summary = "활동 수정", description = "로그인한 유저가 자신이 작성한 활동을 수정할 수 있습니다.")
    @PutMapping("/activity/{id}")
    public ResponseEntity<ApiResponse<ActivityResponseDto>> updateDating(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody ActivityRequestDto requestDto) {
        return activityService.updateActivity(userDetails.getUser(), id, requestDto);
    }

    @Operation(summary = "활동 삭제", description = "로그인한 유저가 자신이 작성한 활동을 삭제할 수 있습니다.")
    @DeleteMapping("/activity/{id}")
    public ResponseEntity<ApiResponse<ActivityResponseDto>> deleteDating(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        return activityService.deleteActivity(userDetails.getUser(), id);
    }
}