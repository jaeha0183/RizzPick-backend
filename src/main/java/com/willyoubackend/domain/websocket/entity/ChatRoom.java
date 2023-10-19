package com.willyoubackend.domain.websocket.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("chatroom")
public class ChatRoom implements Serializable {
    private Long id;
    private List<String> users;
}