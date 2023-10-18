package com.willyoubackend.domain.websocket.controller;

import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.websocket.entity.*;
import com.willyoubackend.domain.websocket.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseDto<ChatRoomResponseDto> createRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.createRoom(chatRoomRequestDto, userDetails.getUser());
    }

    // 채팅방 단일 조회
    @GetMapping("/room/{chatRoomId}")
    public ChatRoom getRoom(@PathVariable Long chatRoomId) {
        return chatRoomService.getRoom(chatRoomId);
    }

    // // 채팅방 인원 추가, 삭제
    @PostMapping("/room/person")
    public ChatRoom setUser(@RequestBody SocketMessage socketMessage) {
        Long chatRoomId = socketMessage.getChatRoomId();
        return chatRoomService.setUser(chatRoomId, socketMessage);
    }
}