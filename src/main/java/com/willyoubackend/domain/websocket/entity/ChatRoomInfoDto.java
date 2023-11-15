package com.willyoubackend.domain.websocket.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChatRoomInfoDto {
    private Long userId;
    private String username;
    private String nickname;
    private String image;

    private List<SocketMessageResponseDto> messages;
}
