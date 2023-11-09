package com.willyoubackend.domain.websocket.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.user_profile.service.UserProfileService;
import com.willyoubackend.domain.websocket.entity.ChatRoom;
import com.willyoubackend.domain.websocket.entity.ChatRoomDto;
import com.willyoubackend.domain.websocket.entity.ChatRoomResponseDto;
import com.willyoubackend.domain.websocket.entity.SocketMessage;
import com.willyoubackend.domain.websocket.repository.ChatMessageRepository;
import com.willyoubackend.domain.websocket.repository.ChatRoomRepository;
import com.willyoubackend.global.dto.ApiResponse;
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
        UserEntity otherUser = chatRoom.getUser1().getId().equals(userId) ? chatRoom.getUser2() : chatRoom.getUser1();
        UserProfileEntity userProfileEntity = otherUser.getUserProfileEntity();
        List<ProfileImageEntity> profileImageEntities = otherUser.getProfileImages();

        String image = (profileImageEntities != null && !profileImageEntities.isEmpty())
                ? profileImageEntities.get(0).getImage()
                : null;

        SocketMessage latestMessage = chatMessageRepository.findTopByChatRoomIdOrderByTimeDesc(chatRoom.getId());

        return ChatRoomDto.builder()
                .chatRoomId(chatRoom.getId())
                .users(Arrays.asList(otherUser.getUsername()))
                .userId(otherUser.getId())
                .nickname(userProfileEntity.getNickname())
                .image(image)
                .birthday(userProfileEntity.getBirthday())
                .intro(userProfileEntity.getIntro())
                .mbti(userProfileEntity.getMbti() != null ? userProfileEntity.getMbti().toString() : null)
                .religion(userProfileEntity.getReligion() != null ? userProfileEntity.getReligion().toString() : null)
                .education(userProfileEntity.getEducation())
                .location(userProfileEntity.getLocation() != null ? userProfileEntity.getLocation().toString() : null)
                .latestMessageId(latestMessage != null ? latestMessage.getId() : null)
                .latestMessage(latestMessage != null ? latestMessage.getMessage() : null)
                .latestMessageTime(latestMessage != null ? latestMessage.getTime().toString() : null)
                .build();
    }
}