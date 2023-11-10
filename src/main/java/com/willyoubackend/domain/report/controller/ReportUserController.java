package com.willyoubackend.domain.report.controller;

import com.willyoubackend.domain.report.dto.ReportUserResponseDto;
import com.willyoubackend.domain.report.service.ReportUserSerivce;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.user_profile.service.ProfileImageService;
import com.willyoubackend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "불건전한 유저 신고", description = "불건전한 유저를 신고하는 URL입니다.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "User ReportUser Controller")
public class ReportUserController {
    private final ReportUserSerivce reportUserSerivce;
    private final UserRepository userRepository;


    @Operation(summary = "신고하기", description = "유저가 다른 유저를 신고할 수 있습니다.")
    @PostMapping("/report/user")
    public ResponseEntity<ApiResponse<ReportUserResponseDto>> createReportUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long reportedUserId,
            @RequestParam String content) {
        UserEntity reporter = userDetails.getUser(); // 현재 로그인한 사용자를 신고자로 사용
        UserEntity reported = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new EntityNotFoundException("Reported user not found"));

        return reportUserSerivce.createReportUser(reporter, reported, content);
    }

    @Operation(summary = "신고내용 전체 조회", description = "신고당한 유저를 전부 조회할 수 있습니다.")
    @GetMapping("/reports/user")
    public ResponseEntity<ApiResponse<List<ReportUserResponseDto>>> getReportUserList(@AuthenticationPrincipal UserDetailsImpl user) {
        return reportUserSerivce.getReportUserList(user);
    }
}