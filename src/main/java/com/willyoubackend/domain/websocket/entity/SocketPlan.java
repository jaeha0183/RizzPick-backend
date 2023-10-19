package com.willyoubackend.domain.websocket.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocketPlan {
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String locationRoadName;
    private String content;
    private Long chatRoomId;
}