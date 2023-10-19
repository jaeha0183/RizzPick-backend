package com.willyoubackend.domain.websocket.controller;

import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.websocket.entity.ChatRoom;
import com.willyoubackend.domain.websocket.entity.ChatRoomDto;
import com.willyoubackend.domain.websocket.entity.SocketMessage;
import com.willyoubackend.domain.websocket.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "내가 속한 체팅방 목록 조회")
    @GetMapping("/rooms/me")
    public List<ChatRoomDto> getMyChatRooms() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        return chatRoomService.findChatRoomsByUsername(username);
    }

    @Operation(summary = "채팅방 단일 조회")
    @GetMapping("/room/{chatRoomId}")
    public ChatRoom getRoom(@PathVariable Long chatRoomId) {
        return chatRoomService.getRoom(chatRoomId);
    }

    @Operation(summary = "채팅방 인원 추가, 삭제")
    @PostMapping("/room/person")
    public ChatRoom setUser(@RequestBody SocketMessage socketMessage) {
        Long chatRoomId = socketMessage.getChatRoomId();
        return chatRoomService.setUser(chatRoomId, socketMessage);
    }
}