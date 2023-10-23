package com.willyoubackend.domain.websocket.controller;

import com.willyoubackend.domain.websocket.entity.SocketMessage;
import com.willyoubackend.domain.websocket.entity.SocketMessageRequsetDto;
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

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/message")
    public void receiveMessage(@Payload SocketMessageRequsetDto socketMessageRequsetDto) {
        Long chatRoomId = socketMessageRequsetDto.getChatRoomId();
        SocketMessage chatMessage = chatMessageService.getMessage(socketMessageRequsetDto);
        simpMessageSendingOperations.convertAndSend("/topic/" + chatRoomId + "/message", chatMessage);
    }
}