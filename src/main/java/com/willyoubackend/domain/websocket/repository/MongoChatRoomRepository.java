package com.willyoubackend.domain.websocket.repository;

import com.willyoubackend.domain.websocket.dto.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoChatRoomRepository extends MongoRepository<ChatRoom, String> {
}
