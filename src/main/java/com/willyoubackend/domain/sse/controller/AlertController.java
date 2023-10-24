package com.willyoubackend.domain.sse.controller;

import com.willyoubackend.domain.sse.dto.AlertResponseDto;
import com.willyoubackend.domain.sse.service.AlertService;
import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AlertController {

    private final AlertService alertService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {
        return alertService.subscribe(userDetails, lastEventId);
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<AlertResponseDto>>> getAlerts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return alertService.getAlerts(userDetails.getUser());
    }

    @PutMapping("/alerts/{id}")
    public ResponseEntity<ApiResponse<AlertResponseDto>> readAlert(@PathVariable Long id) {
        return alertService.readAlert(id);
    }

}
