package com.willyoubackend.domain.user_like_match.repository;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_like_match.entity.UserMatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMatchStatusRepository extends JpaRepository<UserMatchStatus, Long> {
    List<UserMatchStatus> findAllByUserMatchedOneOrUserMatchedTwo(UserEntity userOne, UserEntity userTwo);
}
