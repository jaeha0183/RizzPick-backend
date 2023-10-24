package com.willyoubackend.domain.websocket.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String sender;
    private Long chatRoomId;
    private String message;
    private ZonedDateTime time;
    @Column(nullable = false)
    private boolean isRead = false;

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
