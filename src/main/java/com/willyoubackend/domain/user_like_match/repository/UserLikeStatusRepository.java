package com.willyoubackend.domain.user_like_match.repository;

import com.willyoubackend.domain.user_like_match.entity.UserLikeStatus;
import com.willyoubackend.domain.user_like_match.repository.qdsl.UserLikeStatusCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikeStatusRepository extends JpaRepository<UserLikeStatus, Long>, UserLikeStatusCustomRepository {
}