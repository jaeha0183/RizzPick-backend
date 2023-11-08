package com.willyoubackend.domain.user_like_match.repository;

import com.willyoubackend.domain.user_like_match.entity.UserNopeStatus;
import com.willyoubackend.domain.user_like_match.repository.qdsl.UserNopeStatusCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNopeStatusRepository extends JpaRepository<UserNopeStatus, Long>, UserNopeStatusCustomRepository {
}