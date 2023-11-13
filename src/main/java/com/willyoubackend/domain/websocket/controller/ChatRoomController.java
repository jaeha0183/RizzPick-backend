package com.willyoubackend.domain.websocket.controller;

import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.websocket.entity.ChatRoomDto;
import com.willyoubackend.domain.websocket.entity.ChatRoomInfoDto;
import com.willyoubackend.domain.websocket.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        return chatRoomService.findChatRoomsByUserId(userDetails.getUser().getId());
    }

    @Operation(summary = "특정 체팅방 정보 조회")
    @GetMapping("/rooms/{chatRoomId}")
    public ChatRoomInfoDto getChatRoomInfo(@PathVariable Long chatRoomId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return chatRoomService.findChatRoomInfo(chatRoomId, userDetails.getUser().getId());
    }
}