package com.willyoubackend.domain.websocket.entity;

import lombok.Getter;

@Getter
public class SocketMessageRequsetDto {
    private Status status;
    private Long chatRoomId;
    private String message;
    private String token;
}