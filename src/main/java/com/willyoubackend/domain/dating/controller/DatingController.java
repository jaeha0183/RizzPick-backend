package com.willyoubackend.domain.dating.controller;

import com.amazonaws.Response;
import com.willyoubackend.domain.dating.dto.DatingDetailResponseDto;
import com.willyoubackend.domain.dating.dto.DatingRequestDto;
import com.willyoubackend.domain.dating.dto.DatingResponseDto;
import com.willyoubackend.domain.dating.service.DatingService;
import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.global.dto.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "Dating Controller")
public class DatingController {
    private final DatingService datingService;
    // Create
    // 데이트 작성
    @PostMapping("/dating")
    public ResponseEntity<ApiResponse<DatingResponseDto>> createDating(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return datingService.createDating(userDetails.getUser());
    }

    // Read
    // 데이트 전체 조회
    @GetMapping("/datings")
    public ResponseEntity<ApiResponse<List<DatingResponseDto>>> getDatingList() {
        return datingService.getDatingList();
    }
    // 로그인한 사용자가 작성한 데이트 조회
    @GetMapping("/datings/user")
    public ResponseEntity<ApiResponse<List<DatingResponseDto>>> getDatingListByUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return datingService.getDatingListByUser(userDetails.getUser());
    }
    // 필터링된 데이트 전체 조회 : 지역
    @GetMapping("/datings/location")
    public ResponseEntity<ApiResponse<List<DatingResponseDto>>> getDatingListByLocation(@RequestParam String location) {
        return datingService.getDatingListByLocation(location);
    }

    // 데이트 상세 조회
    @GetMapping("/dating/{id}")
    public ResponseEntity<ApiResponse<DatingDetailResponseDto>> getDatingDetail(@PathVariable Long id) {
        return datingService.getDatingDetail(id);
    }

    // Update
    // 데이트 수정
    @PutMapping("/dating/{id}")
    public ResponseEntity<ApiResponse<DatingResponseDto>> updateDating(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody DatingRequestDto requestDto) {
        return datingService.updateDating(userDetails.getUser(), id, requestDto);
    }
    // Delete
    // 데이트 삭제
    @DeleteMapping("/dating/{id}")
    public ResponseEntity<ApiResponse<DatingResponseDto>> deleteDating(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        return datingService.deleteDating(userDetails.getUser(), id);
    }}
