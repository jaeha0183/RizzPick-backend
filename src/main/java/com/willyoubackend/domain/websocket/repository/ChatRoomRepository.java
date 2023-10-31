package com.willyoubackend.domain.websocket.repository;

import com.willyoubackend.domain.websocket.entity.ChatRoom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
//    Long findChatRoomIdByUser1AndUser2(UserEntity sentUser, UserEntity receivedUser);

    ChatRoom findByUser1_UsernameAndUser2_Username(String username1, String username2);

    @Query("SELECT c FROM ChatRoom c WHERE c.user1.id = :userId OR c.user2.id = :userId")
    List<ChatRoom> findChatRoomsByUserId(@Param("userId") Long userId);
}