package com.willyoubackend.domain.user_profile.repository;

import com.willyoubackend.domain.user_profile.entity.UserRecommendations;
import org.springframework.data.repository.CrudRepository;

public interface UserRecommendationsRepository extends CrudRepository<UserRecommendations, Long> {
    UserRecommendations findByUsername(String username);
}
