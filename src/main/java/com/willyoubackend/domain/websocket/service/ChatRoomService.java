package com.willyoubackend.domain.websocket.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user.service.UserService;
import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.websocket.entity.*;
import com.willyoubackend.domain.websocket.repository.ChatMessageRepository;
import com.willyoubackend.domain.websocket.repository.ChatRoomRedisRepository;
import com.willyoubackend.global.dto.ApiResponse;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.willyoubackend.domain.websocket.entity.Status.JOIN;
import static com.willyoubackend.domain.websocket.entity.Status.LEAVE;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

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

    public List<ChatRoomDto> findChatRoomsByUsername(String username) {
        List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
        Iterable<ChatRoom> allChatRooms = chatRoomRedisRepository.findAll();

        for (ChatRoom chatRoom : allChatRooms) {
            if (chatRoom.getUsers().contains(username)) {
                List<String> otherUsers = new ArrayList<>(chatRoom.getUsers());
                otherUsers.remove(username);

                String otherUsername = otherUsers.get(0);
                Optional<UserEntity> userEntityOpt = userRepository.findByUsername(otherUsername);

                if (userEntityOpt.isPresent()) {
                    UserEntity userEntity = userEntityOpt.get();
                    UserProfileEntity userProfileEntity = userEntity.getUserProfileEntity();
                    List<ProfileImageEntity> profileImageEntities = userEntity.getProfileImages();

                    String nickname = userProfileEntity.getNickname();

                    String image = (profileImageEntities != null && !profileImageEntities.isEmpty())
                            ? profileImageEntities.get(0).getImage()
                            : null;

                    SocketMessage latestMessage = chatMessageRepository.findTopByChatRoomIdOrderByTimeDesc(chatRoom.getId());
                    String latestMessageContent = latestMessage != null ? latestMessage.getMessage() : null;

                    ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                            .chatRoomId(chatRoom.getId())
                            .users(otherUsers)
                            .nickname(nickname)
                            .image(image)
                            .latestMessage(latestMessageContent)
                            .build();

                    chatRoomDtos.add(chatRoomDto);
                }
            }
        }

        return chatRoomDtos;
    }

    public ChatRoom getRoom(Long chatRoomId) {
        return chatRoomRedisRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
    }

    public ChatRoom setUser(Long chatRoomId, SocketMessage socketMessage) {
        ChatRoom chatRoom = getRoom(chatRoomId);

        Claims userInfoFromToken = jwtUtil.getUserInfoFromToken(socketMessage.getToken());
        String username = userInfoFromToken.getSubject();
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