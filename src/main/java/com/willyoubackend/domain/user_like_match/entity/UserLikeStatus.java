package com.willyoubackend.domain.user_like_match.entity;

import com.willyoubackend.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserLikeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_user")
    private UserEntity sentUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_user")
    private UserEntity receivedUser;

    public UserLikeStatus(UserEntity sentUser, UserEntity receivedUser) {
        this.sentUser = sentUser;
        this.receivedUser = receivedUser;
    }
}
