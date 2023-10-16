package com.willyoubackend.domain.websocket.service;

import com.willyoubackend.domain.websocket.dto.ChatMessage;
import com.willyoubackend.domain.websocket.repository.ChatMessageRepository;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {


    private ChatMessageRepository chatMessageRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    public List<ChatMessage> findNotDeletedMessagesByRoomId(String roomId) {
        return chatMessageRepository.findByRoomIdAndIsDeletedFalse(roomId);
    }

    public void deleteMessage(String messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ENTITY)); // 메시지가 없을 경우 예외 발생
        message.setDeletedAt(Date.from(Instant.now()));
        chatMessageRepository.save(message);
        // 삭제 알림 메시지 발송
        sendDeletionNotice();
    }

    private void sendDeletionNotice() {
        ChatMessage deleteNotice = new ChatMessage();
        deleteNotice.setType(ChatMessage.MessageType.TALK);
        deleteNotice.setSender("[알림]");
        deleteNotice.setMessage("A message was deleted.");
        redisTemplate.convertAndSend(channelTopic.getTopic(), deleteNotice);
    }
}