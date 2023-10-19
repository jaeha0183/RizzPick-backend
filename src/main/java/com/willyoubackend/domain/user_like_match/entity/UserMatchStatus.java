package com.willyoubackend.domain.user_like_match.entity;

import com.willyoubackend.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserMatchStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_matched_one")
    private UserEntity userMatchedOne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_matched_two")
    private UserEntity userMatchedTwo;


    public UserMatchStatus(UserEntity userMatchedOne, UserEntity userMatchedTwo) {
        this.userMatchedOne = userMatchedOne;
        this.userMatchedTwo = userMatchedTwo;
    }
}