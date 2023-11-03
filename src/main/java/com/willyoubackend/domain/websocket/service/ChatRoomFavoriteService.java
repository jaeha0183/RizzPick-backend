package com.willyoubackend.domain.websocket.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.websocket.entity.ChatRoom;
import com.willyoubackend.domain.websocket.entity.ChatRoomFavorite;
import com.willyoubackend.domain.websocket.repository.ChatRoomFavoriteRepository;
import com.willyoubackend.domain.websocket.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomFavoriteService {

    @Autowired
    private ChatRoomFavoriteRepository chatRoomFavoriteRepository;

    @Autowired
    private UserRepository userRepository;  // UserEntity를 위한 Repository

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    /**
     * 즐겨찾기에 채팅방을 추가
     *
     * @param userId     사용자 ID
     * @param chatRoomId 채팅방 ID
     */
    public void addToFavorites(Long userId, Long chatRoomId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found"));

        ChatRoomFavorite favorite = new ChatRoomFavorite();
        favorite.setUser(user);
        favorite.setChatRoom(chatRoom);

        chatRoomFavoriteRepository.save(favorite);
    }

    /**
     * 즐겨찾기에서 채팅방을 제거
     *
     * @param userId     사용자 ID
     * @param chatRoomId 채팅방 ID
     */
    public void removeFromFavorites(Long userId, Long chatRoomId) {
        List<ChatRoomFavorite> favorites = chatRoomFavoriteRepository.findByUserId(userId);
        ChatRoomFavorite favoriteToDelete = favorites.stream()
                .filter(f -> f.getChatRoom().getId().equals(chatRoomId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Favorite not found for the given ChatRoom and User"));

        chatRoomFavoriteRepository.delete(favoriteToDelete);
    }


//     사용자의 즐겨찾기 채팅방 목록을 조회


}


