package com.willyoubackend.domain.websocket.controller;

import com.willyoubackend.domain.websocket.entity.SocketMessageResponseDto;
import com.willyoubackend.domain.websocket.service.ChatMessageService;
import com.willyoubackend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    // 이전 메세지들 불러오기
    @Operation(summary = "이전 메세지들 불러오기 ")
    @GetMapping("/message/{chatRoomId}")
    public ApiResponse<List<SocketMessageResponseDto>> getMessages(@PathVariable Long chatRoomId) {
        return chatMessageService.getMessages(chatRoomId);
    }
}