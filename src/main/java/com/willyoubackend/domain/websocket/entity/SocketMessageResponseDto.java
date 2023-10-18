package com.willyoubackend.domain.websocket.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class SocketMessageResponseDto {

    private Long chatRoomId; //FE에서 전달
    private String sender; // token에서 발췌
    private String message; //FE에서 전달
    private ZonedDateTime time; //BE에서 생성
}
