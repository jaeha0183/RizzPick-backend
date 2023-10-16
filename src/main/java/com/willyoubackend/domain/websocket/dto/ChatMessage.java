package com.willyoubackend.domain.websocket.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "chatMessages")
@Getter
@Setter
public class ChatMessage { // 채팅 메시지 모델

    @Id
    private String id;
    private Date createdAt;

    public enum MessageType {
        ENTER, JOIN, TALK
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private boolean isDeleted; // 삭제 여부 필드 추가
    private Date deletedAt; // 삭제 날짜 필드 추가
}
