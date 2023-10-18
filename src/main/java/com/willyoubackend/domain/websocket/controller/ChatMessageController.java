package com.willyoubackend.domain.websocket.controller;

import com.willyoubackend.domain.websocket.entity.ResponseDto;
import com.willyoubackend.domain.websocket.entity.SocketMessageResponseDto;
import com.willyoubackend.domain.websocket.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    // 이전 메세지 전송
    @GetMapping("/message/{chatRoomId}")
    public ResponseDto<List<SocketMessageResponseDto>> getMessages(@PathVariable Long chatRoomId) {
        return chatMessageService.getMessages(chatRoomId);
    }
}