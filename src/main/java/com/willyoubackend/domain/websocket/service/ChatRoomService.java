package com.willyoubackend.domain.websocket.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.websocket.entity.*;
import com.willyoubackend.domain.websocket.repository.ChatMessageRepository;
import com.willyoubackend.domain.websocket.repository.ChatRoomRepository;
import com.willyoubackend.global.dto.ApiResponse;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ApiResponse<ChatRoomResponseDto> createRoom(UserEntity sentUser, UserEntity receivedUser) {
        ChatRoom chatRoom = ChatRoom.builder()
                .user1(sentUser)
                .user2(receivedUser)
                .build();

        chatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .build();

        return ApiResponse.successData(chatRoomResponseDto);
    }

    @Transactional
    public List<ChatRoomDto> findChatRoomsByUserId(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUserId(userId);
        return chatRooms.stream()
                .map(chatRoom -> convertToDto(chatRoom, userId))
                .sorted(Comparator.comparing(ChatRoomDto::getLatestMessageTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    private ChatRoomDto convertToDto(ChatRoom chatRoom, Long userId) {
        UserEntity oppositeUser = findOppositeUser(chatRoom, userId);
        UserProfileEntity userProfileEntity = oppositeUser.getUserProfileEntity();
        List<ProfileImageEntity> profileImageEntities = oppositeUser.getProfileImages();

        String image = (profileImageEntities != null && !profileImageEntities.isEmpty())
                ? profileImageEntities.get(0).getImage()
                : null;

        SocketMessage latestMessage = chatMessageRepository.findTopByChatRoomIdOrderByTimeDesc(chatRoom.getId());

        return ChatRoomDto.builder()
                .chatRoomId(chatRoom.getId())
                .users(Arrays.asList(oppositeUser.getUsername()))
                .userId(oppositeUser.getId())
                .nickname(userProfileEntity.getNickname())
                .image(image)
                .birthday(userProfileEntity.getBirthday())
                .intro(userProfileEntity.getIntro())
                .mbti(userProfileEntity.getMbti() != null ? userProfileEntity.getMbti().toString() : null)
                .religion(userProfileEntity.getReligion() != null ? userProfileEntity.getReligion().toString() : null)
                .hobby(userProfileEntity.getHobby())
                .interest(userProfileEntity.getInterest())
                .location(userProfileEntity.getLocation() != null ? userProfileEntity.getLocation().toString() : null)
                .latestMessageId(latestMessage != null ? latestMessage.getId() : null)
                .latestMessage(latestMessage != null ? latestMessage.getMessage() : null)
                .latestMessageTime(latestMessage != null ? latestMessage.getTime().toString() : null)
                .build();
    }

    public ChatRoomInfoDto findChatRoomInfo(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        UserEntity oppositeUser = findOppositeUser(chatRoom, userId);

        List<SocketMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);
        List<SocketMessageResponseDto> messageDtos = messages.stream()
                .map(message -> SocketMessageResponseDto.builder()
                        .chatRoomId(chatRoomId)
                        .sender(message.getSender())
                        .messageId(message.getId())
                        .message(message.getMessage())
                        .time(message.getTime())
                        .build())
                .collect(Collectors.toList());

        return ChatRoomInfoDto.builder()
                .userId(oppositeUser.getId())
                .username(oppositeUser.getUsername())
                .nickname(oppositeUser.getUserProfileEntity().getNickname())
                .image(oppositeUser.getProfileImages().isEmpty() ? null : oppositeUser.getProfileImages().get(0).getImage())
                .messages(messageDtos)
                .build();
    }


    private UserEntity findOppositeUser(ChatRoom chatRoom, Long id) {
        UserEntity oppositeUser;
        if (chatRoom.getUser1().getId().equals(id)) {
            oppositeUser = chatRoom.getUser2();
        } else if (chatRoom.getUser2().getId().equals(id)) {
            oppositeUser = chatRoom.getUser1();
        } else {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return oppositeUser;
    }
}