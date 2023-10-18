package com.willyoubackend.domain.websocket.entity;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class SocketMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Status status; //FE에서 전달, JOIN, MESSAGE, LEAVE
    private String sender; // token에서 발췌
    private Long chatRoomId; //FE에서 전달
    private String message; //FE에서 전달
    private ZonedDateTime time; //BE에서 생성
    private String token;
}
