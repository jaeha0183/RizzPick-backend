package com.willyoubackend.domain.websocket.entity;

import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocketMessageDto {
    private Long chatRoomId;
    private String sender;
    private String message;
    private ZonedDateTime time;
}
