package com.willyoubackend.domain.websocket.controller;

import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.websocket.dto.ChatMessage;
import com.willyoubackend.domain.websocket.repository.ChatMessageRepository;
import com.willyoubackend.domain.websocket.service.ChatMessageService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    private final JwtUtil jwtUtil;
    private final ChatMessageRepository chatMessageRepository;
    private ListOperations<String, Object> listOpsChatMessage;
    private ChatMessageService chatMessageService;


    @PostConstruct
    private void init() {
        listOpsChatMessage = redisTemplate.opsForList();
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("token") String token) {
        Claims username = jwtUtil.getUserInfoFromToken(token);
        String userId = username.get("sub").toString();
        message.setSender(userId);
        message.setCreatedAt(Date.from(Instant.now()));

        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setSender("[알림]");
            message.setMessage(userId + "님이 입장하셨습니다.");
        }

        // MongoDB에 메시지 저장
        chatMessageRepository.save(message);

        // Redis List에 메시지 추가
        // listOpsChatMessage.leftPush(message.getRoomId(), message);

        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }

    @MessageMapping("/chat/deleteMessage") // 메시지 삭제
    public void deleteMessage(String messageId, @Header("token") String token) {
        chatMessageService.deleteMessage(messageId);
    }

    @GetMapping("/room/{roomId}/messages") // 채팅방의 모든 메시지 조회
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable String roomId) {
        List<ChatMessage> messages = chatMessageService.findNotDeletedMessagesByRoomId(roomId);
        return ResponseEntity.ok(messages);
    }
}