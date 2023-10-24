package com.willyoubackend.domain.websocket.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class SocketMessageResponseDto {
    private String sender;
    private String message;
    private ZonedDateTime time;
}
