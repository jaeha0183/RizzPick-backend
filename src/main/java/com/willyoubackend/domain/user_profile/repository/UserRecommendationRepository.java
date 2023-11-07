package com.willyoubackend.domain.user_profile.repository;

import com.willyoubackend.domain.user_profile.entity.UserRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRecommendationRepository extends JpaRepository<UserRecommendation, Long> {
}
