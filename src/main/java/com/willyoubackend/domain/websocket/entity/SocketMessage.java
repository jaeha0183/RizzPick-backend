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

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    private String sender;
    private String message;
    private ZonedDateTime time;
    @Column(nullable = false)
    @Builder.Default
    private boolean isRead = false;

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
