package com.willyoubackend.domain.websocket.repository;

import com.willyoubackend.domain.websocket.entity.SocketMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<SocketMessage, Long> {
    List<SocketMessage> findAllByChatRoomId(Long chatRoomId);

    SocketMessage findTopByChatRoomIdOrderByTimeDesc(Long chatRoomId);

    void deleteByChatRoomId(Long chatRoomId);
}