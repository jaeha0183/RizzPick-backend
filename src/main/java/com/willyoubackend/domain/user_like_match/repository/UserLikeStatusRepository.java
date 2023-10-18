package com.willyoubackend.domain.user_like_match.repository;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_like_match.entity.UserLikeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLikeStatusRepository extends JpaRepository<UserLikeStatus, Long> {
    UserLikeStatus findBySentUserAndReceivedUser(UserEntity sentUser, UserEntity receivedUser);
    List<UserLikeStatus> findAllBySentUser(UserEntity sentUser);
    List<UserLikeStatus> findAllByReceivedUser(UserEntity receivedUser);

    Boolean existsByReceivedUser(UserEntity user);
}
