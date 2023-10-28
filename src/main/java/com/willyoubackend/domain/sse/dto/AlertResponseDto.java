package com.willyoubackend.domain.sse.dto;

import com.willyoubackend.domain.sse.entity.Alert;
import com.willyoubackend.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AlertResponseDto {
    private Long id;
    private UserDto receiver;
    private UserDto sender;
    private String message;
    private String url;
    private boolean readStatus;

    public AlertResponseDto (Alert alert){
        this.id = alert.getId();
        this.receiver = new UserDto(alert.getReceiver());
        this.sender = new UserDto(alert.getSender());
        this.message = alert.getMessage();
        this.url = alert.getUrl();
        this.readStatus = alert.isReadStatus();
    }


}
