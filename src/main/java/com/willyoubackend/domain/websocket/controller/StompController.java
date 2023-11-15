package com.willyoubackend.domain.websocket.controller;

import com.willyoubackend.domain.websocket.entity.ReadMessagePayload;
import com.willyoubackend.domain.websocket.entity.SocketMessage;
import com.willyoubackend.domain.websocket.entity.SocketMessageRequsetDto;
import com.willyoubackend.domain.websocket.entity.SocketMessageResponseDto;
import com.willyoubackend.domain.websocket.service.ChatMessageService;
import com.willyoubackend.domain.websocket.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequiredArgsConstructor
@Slf4j(topic = "Message")
public class StompController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/message")
    public void receiveMessage(@Payload SocketMessageRequsetDto socketMessageRequsetDto) {
        log.info("receiveMessage " + socketMessageRequsetDto.getMessage());
        Long chatRoomId = socketMessageRequsetDto.getChatRoomId();
        SocketMessage socketMessage = chatMessageService.saveMessage(socketMessageRequsetDto);
        log.info("receiveMessage " + socketMessage.getSender());
        SocketMessageResponseDto chatMessage = SocketMessageResponseDto.builder()
                .chatRoomId(chatRoomId)
                .sender(socketMessage.getSender())
                .time((socketMessage.getTime()))
                .message(socketMessage.getMessage())
                .build();
        simpMessageSendingOperations.convertAndSend("/topic/" + chatRoomId + "/message", chatMessage);
    }

    @MessageMapping("/readMessage")
    public void handleReadMessage(@Payload ReadMessagePayload payload) {
        Long messageId = payload.getMessageId();
        String sender = payload.getSender();
        chatMessageService.markMessageAsRead(messageId, sender);
    }

}