package com.willyoubackend.domain.websocket.service;

import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.user.service.UserService;
import com.willyoubackend.domain.websocket.entity.SocketMessage;
import com.willyoubackend.domain.websocket.entity.SocketMessageRequsetDto;
import com.willyoubackend.domain.websocket.entity.SocketMessageResponseDto;
import com.willyoubackend.domain.websocket.repository.ChatMessageRepository;
import com.willyoubackend.global.dto.ApiResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final UserService userService;

    // 메세지 저장
    public SocketMessage getMessage(SocketMessageRequsetDto socketMessageRequsetDto) {
        Claims userInfoFromToken = jwtUtil.getUserInfoFromToken(socketMessageRequsetDto.getToken());
        String username = userInfoFromToken.getSubject();
        // time 추가
        ZonedDateTime time = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        SocketMessage socketMessage = SocketMessage.builder()
                .chatRoomId(socketMessageRequsetDto.getChatRoomId())
                .sender(username)
                .time(time)
                .message(socketMessageRequsetDto.getMessage())
                .build();

        chatMessageRepository.save(socketMessage);

        return socketMessage;
    }

    // 이전 메세지 전송
    public ApiResponse<List<SocketMessageResponseDto>> getMessages(Long chatRoomId) {
        List<SocketMessage> socketMessageList = chatMessageRepository.findAllByChatRoomId(chatRoomId);

        List<SocketMessageResponseDto> socketMessageResponseDtoList = new ArrayList<>();

        for (SocketMessage socketMessage : socketMessageList) {
            socketMessageResponseDtoList.add(SocketMessageResponseDto.builder()
                    .sender(socketMessage.getSender())
                    .message(socketMessage.getMessage())
                    .time(socketMessage.getTime())
                    .build()
            );
        }
        return ApiResponse.successData(socketMessageResponseDtoList);
    }
}