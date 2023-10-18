package com.willyoubackend.domain.websocket.repository;

import com.willyoubackend.domain.websocket.entity.ChatRoom;
import org.springframework.data.repository.CrudRepository;

public interface ChatRoomRedisRepository extends CrudRepository<ChatRoom, Long> {

}