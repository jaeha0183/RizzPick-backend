package com.willyoubackend.domain.user.entity;

import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.websocket.entity.ChatRoom;
import com.willyoubackend.domain.websocket.entity.ChatRoomFavorite;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private Long kakaoId;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private UserProfileEntity userProfileEntity;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<ProfileImageEntity> profileImages;

    // 사용자가 참여하는 채팅방 목록
    @OneToMany(mappedBy = "user1")
    private List<ChatRoom> chatroomsAsUser1;

    @OneToMany(mappedBy = "user2")
    private List<ChatRoom> chatroomsAsUser2;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomFavorite> favoriteRooms = new ArrayList<>();

    public UserEntity(String username, String password, String email, UserRoleEnum role, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public UserEntity(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public UserEntity kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    // 사용자가 참여하는 채팅방 리스트를 받아오는 편의 메서드
    public List<ChatRoom> getChatrooms() {
        List<ChatRoom> combined = new ArrayList<>(chatroomsAsUser1);
        combined.addAll(chatroomsAsUser2);
        return combined;
    }

    public void setPassword(String encode) {
        this.password = encode;
    }
}