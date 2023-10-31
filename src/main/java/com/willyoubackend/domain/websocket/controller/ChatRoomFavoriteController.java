package com.willyoubackend.domain.websocket.controller;

import com.willyoubackend.domain.websocket.entity.ChatRoom;
import com.willyoubackend.domain.websocket.entity.FavoriteChatRoomsResponseDto;
import com.willyoubackend.domain.websocket.service.ChatRoomFavoriteService;
import com.willyoubackend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class ChatRoomFavoriteController {

    @Autowired
    private ChatRoomFavoriteService chatRoomFavoriteService;

    @Operation(summary = "채팅방 즐겨찾기에 추가")
    @PostMapping("/add")
    public ApiResponse<Object> addToFavorites(@RequestParam Long userId, @RequestParam Long chatRoomId) {
        chatRoomFavoriteService.addToFavorites(userId, chatRoomId);
        return ApiResponse.successMessage("해당 채팅방을 즐겨찾기에 추가했습니다.");
    }

    @Operation(summary = "채팅방 즐겨찾기에서 제거")
    @DeleteMapping("/remove")
    public ApiResponse<Object> removeFromFavorites(@RequestParam Long userId, @RequestParam Long chatRoomId) {
        chatRoomFavoriteService.removeFromFavorites(userId, chatRoomId);
        return ApiResponse.successMessage("해당 채팅방을 즐겨찾기에서 제거했습니다.");
    }

}

