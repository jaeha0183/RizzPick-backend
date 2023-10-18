package com.willyoubackend.domain.websocket.controller;

import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.user.service.UserService;
import com.willyoubackend.domain.websocket.entity.ChatRoom;
import com.willyoubackend.domain.websocket.entity.SocketMessage;
import com.willyoubackend.domain.websocket.entity.SocketMessageRequsetDto;
import com.willyoubackend.domain.websocket.entity.SocketPlan;
import com.willyoubackend.domain.websocket.service.ChatMessageService;
import com.willyoubackend.domain.websocket.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/message") // /app/socketMessage 로 받으면
// @SendTo("/topic/socketMessage") // return 값을 /topic/socketMessage 로 넘겨준다.
    public void receiveMessage(@Payload SocketMessageRequsetDto socketMessageRequsetDto) {
        Long chatRoomId = socketMessageRequsetDto.getChatRoomId();
        SocketMessage chatMessage = chatMessageService.getMessage(socketMessageRequsetDto);
        simpMessageSendingOperations.convertAndSend("/topic/" + chatRoomId + "/message", chatMessage);
    }

    @MessageMapping("/plan")
    public void receivePlan(@Payload SocketPlan socketPlan) {
        Long chatRoomId = socketPlan.getChatRoomId();
        simpMessageSendingOperations.convertAndSend("/topic/" + chatRoomId + "/plan", socketPlan);
    }

    @MessageMapping("/user")
    public void receiveUser(@Payload SocketMessage socketMessage) {
        Long chatRoomId = socketMessage.getChatRoomId();
        ChatRoom chatRoom = chatRoomService.setUser(chatRoomId, socketMessage);
        simpMessageSendingOperations.convertAndSend("/topic/" + chatRoomId + "/user", chatRoom.getUsers());
    }
}