package com.willyoubackend.domain.websocket.service;

import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.websocket.entity.ChatRoom;
import com.willyoubackend.domain.websocket.entity.SocketMessage;
import com.willyoubackend.domain.websocket.entity.SocketMessageRequsetDto;
import com.willyoubackend.domain.websocket.entity.SocketMessageResponseDto;
import com.willyoubackend.domain.websocket.repository.ChatMessageRepository;
import com.willyoubackend.domain.websocket.repository.ChatRoomRepository;
import com.willyoubackend.global.dto.ApiResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "Chat Message Service")
@RequiredArgsConstructor
public class ChatMessageService {

    private final JwtUtil jwtUtil;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public SocketMessage saveMessage(SocketMessageRequsetDto socketMessageRequsetDto) {
        Claims userInfoFromToken = jwtUtil.getUserInfoFromToken(socketMessageRequsetDto.getToken());
        String username = userInfoFromToken.getSubject();
        ZonedDateTime time = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        Long chatRoomId = socketMessageRequsetDto.getChatRoomId();

        // ChatRoom 엔터티 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found"));

        SocketMessage socketMessage = SocketMessage.builder()
                .chatRoom(chatRoom)  // chatRoom 인스턴스 사용
                .sender(username)
                .time(time)
                .message(socketMessageRequsetDto.getMessage())
                .build();

        chatMessageRepository.save(socketMessage);

        return socketMessage;
    }


    @Transactional
    public void markMessageAsRead(Long messageId, String senderUsername) {
        SocketMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSender().equals(senderUsername)) {
            message.setIsRead(true);
        }

        List<SocketMessage> olderMessages = chatMessageRepository
                .findAllByChatRoomIdAndSenderAndIsReadFalseAndTimeBefore(
                        message.getChatRoom().getId(),
                        message.getSender(),
                        message.getTime()
                );

        for (SocketMessage olderMessage : olderMessages) {
            olderMessage.setIsRead(true);
        }

        chatMessageRepository.save(message);
        chatMessageRepository.saveAll(olderMessages);
    }


    public ApiResponse<List<SocketMessageResponseDto>> getMessages(Long chatRoomId) {
        List<SocketMessage> socketMessageList = chatMessageRepository.findAllByChatRoomId(chatRoomId);

        List<SocketMessageResponseDto> socketMessageResponseDtoList = new ArrayList<>();

        for (SocketMessage socketMessage : socketMessageList) {
            socketMessageResponseDtoList.add(SocketMessageResponseDto.builder()
                    .chatRoomId(socketMessage.getChatRoom().getId())
                    .sender(socketMessage.getSender())
                    .messageId(socketMessage.getId())
                    .message(socketMessage.getMessage())
                    .time(socketMessage.getTime())
                    .build()
            );
        }
        return ApiResponse.successData(socketMessageResponseDtoList);
    }
}