package com.willyoubackend.domain.websocket.repository;

import com.willyoubackend.domain.websocket.dto.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByRoomIdAndIsDeletedFalse(String roomId); // 채팅방의 메시지 중 삭제되지 않은 메시지 조회

}