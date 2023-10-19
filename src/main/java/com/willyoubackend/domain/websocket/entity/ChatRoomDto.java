package com.willyoubackend.domain.websocket.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChatRoomDto {
    private Long chatRoomId;
    private List<String> users;
    private String nickname;
    private String image;
    private String latestMessage;
}
