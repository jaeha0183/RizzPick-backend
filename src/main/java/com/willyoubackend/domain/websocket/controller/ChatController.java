package com.willyoubackend.domain.websocket.controller;

import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.websocket.dto.ChatMessage;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    private final JwtUtil jwtUtil;
    private ListOperations<String, Object> listOpsChatMessage;

    @PostConstruct
    private void init() {
        listOpsChatMessage = redisTemplate.opsForList();
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("token") String token) {
        Claims username = jwtUtil.getUserInfoFromToken(token);
        String userId = username.get("sub").toString();
        message.setSender(userId);
        // 채팅방 입장시에는 대화명과 메시지를 자동으로 세팅한다.
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setSender("[알림]");
            message.setMessage(userId + "님이 입장하셨습니다.");
        }
        // Redis List에 메시지 추가
        listOpsChatMessage.leftPush(message.getRoomId(), message);

        // 채팅방 메시지 10초마다 삭제
//        redisTemplate.expire(message.getRoomId(), 20, TimeUnit.SECONDS);

        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}