package com.willyoubackend.domain.websocket.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomResponseDto {
    private Long chatRoomId;
}