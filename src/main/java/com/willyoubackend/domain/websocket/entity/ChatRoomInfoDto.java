package com.willyoubackend.domain.websocket.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomInfoDto {
    Long userId;
    String nickname;
    String image;
}
