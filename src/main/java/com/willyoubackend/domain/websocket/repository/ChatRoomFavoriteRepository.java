package com.willyoubackend.domain.websocket.repository;

import com.willyoubackend.domain.websocket.entity.ChatRoomFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomFavoriteRepository extends JpaRepository<ChatRoomFavorite, Long> {
    List<ChatRoomFavorite> findByUserId(Long userId);
}
