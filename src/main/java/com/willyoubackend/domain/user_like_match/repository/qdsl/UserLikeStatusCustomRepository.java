package com.willyoubackend.domain.user_like_match.repository.qdsl;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_like_match.entity.UserLikeStatus;

import java.util.List;

public interface UserLikeStatusCustomRepository {
    UserLikeStatus findBySentUserAndReceivedUser(UserEntity sentUser, UserEntity receivedUser);

    List<UserLikeStatus> findAllBySentUser(UserEntity sentUser);

    List<UserLikeStatus> findAllByReceivedUser(UserEntity receivedUser);

    Boolean existsByReceivedUser(UserEntity user);
}
