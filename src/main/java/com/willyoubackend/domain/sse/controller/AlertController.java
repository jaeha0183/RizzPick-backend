package com.willyoubackend.domain.sse.controller;

import com.willyoubackend.domain.sse.dto.AlertResponseDto;
import com.willyoubackend.domain.sse.service.AlertService;
import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Tag(name = "알림", description = "sse")
@Slf4j
@RequiredArgsConstructor
@RestController("/sse")
public class AlertController {

    private final AlertService alertService;

    // SSE 연결
    @Operation(summary = "sse 구독")
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId,
            HttpServletResponse response) {
        // nginx 리버스 프록시에서 버퍼링 기능으로 인한 오동작 방지
        response.setHeader("X-Accel-Buffering", "no");
        return alertService.subscribe(userDetails, lastEventId);
    }

    // 전체 알림 조회
    @Operation(summary = "전체 알림 조회")
    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<AlertResponseDto>>> getAlerts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return alertService.getAlerts(userDetails.getUser());
    }

    // 알림 읽음 표시
    @Operation(summary = "알림 읽음 표시")
    @PutMapping("/alerts/{id}")
    public ResponseEntity<ApiResponse<AlertResponseDto>> readAlert(@PathVariable Long id) {
        return alertService.readAlert(id);
    }

}
