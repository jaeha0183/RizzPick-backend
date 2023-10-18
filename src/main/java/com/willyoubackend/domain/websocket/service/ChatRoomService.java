package com.willyoubackend.domain.websocket.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.user.service.UserService;
import com.willyoubackend.domain.websocket.entity.*;
import com.willyoubackend.domain.websocket.repository.ChatRoomRedisRepository;
import com.willyoubackend.global.dto.ApiResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.willyoubackend.domain.websocket.entity.Status.JOIN;
import static com.willyoubackend.domain.websocket.entity.Status.LEAVE;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;

    // 매치된 채팅방 생성
    public ApiResponse<ChatRoomResponseDto> createRoom(ChatRoomRequestDto chatRoomRequestDto, UserEntity sentUser, UserEntity receivedUser) {

        Long chatRoomId = chatRoomRequestDto.getChatRoomId();

        List<String> users = new ArrayList<>();
        users.add(sentUser.getUsername());
        users.add(receivedUser.getUsername());

        ChatRoom chatRoom = ChatRoom.builder()
                .id(chatRoomId)
                .users(users)
                .build();

        chatRoomRedisRepository.save(chatRoom);

        ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .build();

        return ApiResponse.successData(chatRoomResponseDto);
    }

    // 유저가 속한 채팅방 목록 조회
    public List<ChatRoom> findChatRoomsByUsername(String username) {
        List<ChatRoom> chatRooms = new ArrayList<>();
        Iterable<ChatRoom> allChatRooms = chatRoomRedisRepository.findAll();

        for (ChatRoom chatRoom : allChatRooms) {
            if (chatRoom.getUsers().contains(username)) {
                chatRooms.add(chatRoom);
            }
        }

        return chatRooms;
    }

    // 채팅방 단일 조회
    public ChatRoom getRoom(Long chatRoomId) {
        return chatRoomRedisRepository.findById(chatRoomId).get();
    }

    // 채팅방 인원 추가, 삭제
    public ChatRoom setUser(Long chatRoomId, SocketMessage socketMessage) {
        ChatRoom chatRoom = chatRoomRedisRepository.findById(chatRoomId).get();
        Claims userInfoFromToken = jwtUtil.getUserInfoFromToken(socketMessage.getToken());
        String username = userInfoFromToken.getSubject();
        //null값 예외처리추가
        Status status = socketMessage.getStatus();
        List<String> userList = chatRoom.getUsers();

        if (status.equals(JOIN) && !(userList.contains(username))) {
            userList.add(username);

        } else if (status.equals(LEAVE) && userList.contains(username)) {
            userList.remove(username);

        }
        chatRoom.setUsers(userList);

        chatRoomRedisRepository.save(chatRoom);

        return chatRoom;
    }

}