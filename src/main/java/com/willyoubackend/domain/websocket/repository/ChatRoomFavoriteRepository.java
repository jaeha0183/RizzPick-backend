package com.willyoubackend.domain.websocket.repository;

import com.willyoubackend.domain.websocket.entity.ChatRoomFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomFavoriteRepository extends JpaRepository<ChatRoomFavorite, Long> {
    List<ChatRoomFavorite> findByUserId(Long userId);
}
