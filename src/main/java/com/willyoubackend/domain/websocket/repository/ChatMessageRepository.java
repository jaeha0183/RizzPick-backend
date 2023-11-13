package com.willyoubackend.domain.websocket.repository;

import com.willyoubackend.domain.websocket.entity.SocketMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<SocketMessage, Long> {
    List<SocketMessage> findAllByChatRoomId(Long chatRoomId);

    SocketMessage findTopByChatRoomIdOrderByTimeDesc(Long chatRoomId);

    void deleteByChatRoomId(Long chatRoomId);

    List<SocketMessage> findAllByTimeBeforeAndIsReadFalse(ZonedDateTime time);

    List<SocketMessage> findByChatRoomId(Long chatRoomId);
}