package com.willyoubackend.domain.websocket.entity;

import lombok.Getter;

@Getter
public class SocketMessageRequsetDto {

    private Status status; //FE에서 전달, JOIN, MESSAGE, LEAVE
    private Long chatRoomId; //FE에서 전달
    private String message; //FE에서 전달
    private String token;
}
