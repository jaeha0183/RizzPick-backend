package com.willyoubackend.domain.websocket.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.websocket.entity.*;
import com.willyoubackend.domain.websocket.repository.ChatMessageRepository;
import com.willyoubackend.domain.websocket.repository.ChatRoomRedisRepository;
import com.willyoubackend.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

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
                    Integer age = userProfileEntity.getAge();
                    String intro = userProfileEntity.getIntro();
                    String mbti = userProfileEntity.getMbti() != null ? userProfileEntity.getMbti().toString() : null;
                    String religion = userProfileEntity.getReligion() != null ? userProfileEntity.getReligion().toString() : null;
                    String education = userProfileEntity.getEducation();
                    String location = userProfileEntity.getLocation() != null ? userProfileEntity.getLocation().toString() : null;

                    String image = (profileImageEntities != null && !profileImageEntities.isEmpty())
                            ? profileImageEntities.get(0).getImage()
                            : null;

                    SocketMessage latestMessage = chatMessageRepository.findTopByChatRoomIdOrderByTimeDesc(chatRoom.getId());
                    Long latestMessageId = latestMessage != null ? latestMessage.getId() : null;
                    String latestMessageContent = latestMessage != null ? latestMessage.getMessage() : null;
                    String latestMessageTime = latestMessage != null ? latestMessage.getTime().toString() : null;

                    ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                            .chatRoomId(chatRoom.getId())
                            .users(otherUsers)
                            .nickname(nickname)
                            .image(image)
                            .age(age)
                            .intro(intro)
                            .mbti(mbti)
                            .religion(religion)
                            .education(education)
                            .location(location)
                            .latestMessageId(latestMessageId)
                            .latestMessage(latestMessageContent)
                            .latestMessageTime(latestMessageTime)
                            .build();

                    chatRoomDtos.add(chatRoomDto);
                }
            }
        }

        // Sort chatRoomDtos by latestMessageTime in descending order
        chatRoomDtos.sort(Comparator.comparing(ChatRoomDto::getLatestMessageTime, Comparator.nullsLast(Comparator.reverseOrder())));

        return chatRoomDtos;
    }

}