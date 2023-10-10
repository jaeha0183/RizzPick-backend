package com.willyoubackend.domain.user_like_match.repository;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_like_match.entity.UserNopeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNopeStatusRepository extends JpaRepository<UserNopeStatus, Long> {
    UserNopeStatus findBySentUserAndReceivedUser(UserEntity sentUser, UserEntity receivedUser);
}
