package com.willyoubackend.domain.user_profile.entity;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.dto.UserProfileResponseDto;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

@Getter
@RedisHash(value = "User Recommendations", timeToLive = 3600)
public class UserRecommendations {
    @Id
    private Long id;
    @Indexed
    private String username;

    private List<UserProfileResponseDto> recommendedUsers;

    public UserRecommendations(String username, List<UserProfileResponseDto> recommendedUsers) {
        this.username = username;
        this.recommendedUsers = recommendedUsers;
    }
}
