package com.willyoubackend.domain.sse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.willyoubackend.domain.sse.entity.Alert;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class AlertResponseDto {
    private Long id;
    private UserDto receiver;
    private UserDto sender;
    private String message;
    private String url;
    private boolean readStatus;
    private String time;

    public AlertResponseDto(Alert alert) {
        this.id = alert.getId();
        this.receiver = new UserDto(alert.getReceiver());
        this.sender = new UserDto(alert.getSender());
        this.message = alert.getMessage();
        this.url = alert.getUrl();
        this.readStatus = alert.isReadStatus();
        this.time = alert.getTime().toString();
    }


}