package com.willyoubackend.domain.websocket.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.user.service.UserService;
import com.willyoubackend.domain.websocket.entity.*;
import com.willyoubackend.domain.websocket.repository.ChatRoomRedisRepository;
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

    // 채팅방 생성
    public ResponseDto<ChatRoomResponseDto> createRoom(ChatRoomRequestDto chatRoomRequestDto, UserEntity userentity) {

        Long chatRoomId = chatRoomRequestDto.getChatRoomId();
        String username = userentity.getUsername();

// 채팅방 없을시 저장
        if (chatRoomRedisRepository.findById(chatRoomId).isEmpty()) {

// 초기값 생성, 초기값 없을시 NullpointException

            List<String> users = new ArrayList<>();
            users.add("chatRoomId" + chatRoomId);
            users.add("username" + username);

            ChatRoom chatRoom = ChatRoom.builder()
                    .id(chatRoomRequestDto.getChatRoomId())
                    .users(users)
                    .build();

            chatRoomRedisRepository.save(chatRoom);
        }

        ChatRoom chatRoomRepo = chatRoomRedisRepository.findById(chatRoomId).get();
        ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder()
                .chatRoomId(chatRoomRepo.getId())
                .build();

        // 채팅방 Id값 리턴
        return ResponseDto.success(chatRoomResponseDto);
    }

    // 채팅방 단일 조회
    public ChatRoom getRoom(Long chatRoomId) {
        return chatRoomRedisRepository.findById(chatRoomId).get();
    }

    // 채팅방 인원 추가, 삭제
    public ChatRoom setUser(Long chatRoomId, SocketMessage socketMessage) {

        ChatRoom chatRoom = chatRoomRedisRepository.findById(chatRoomId).get();
        //null값 예외처리추가
        Status status = socketMessage.getStatus();

        // token 으로 userId 추출 -----> userId 로 닉네임 추출
        String userId = String.valueOf((jwtUtil.getUserInfoFromToken(socketMessage.getToken())));
        String username = userService.getUserNameById(Long.valueOf(userId));
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