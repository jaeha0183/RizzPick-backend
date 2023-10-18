package com.willyoubackend.domain.websocket.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChatRoomDto {

    private Long chatRoomId; // 채팅방 ID

    private List<String> users; // 채팅방에 있는 사용자들

    private String nickname; // 현재 인증된 사용자가 아닌 같은 방의 다른 사용자의 닉네임

    private String image; // 현재 인증된 사용자의 프로필 정보

}
