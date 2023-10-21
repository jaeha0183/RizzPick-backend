package com.willyoubackend.domain.user.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "Refresh Token", timeToLive = 3 * 24 * 60 * 60 * 1000L)
public class RefreshToken {
    @Id
    private String id;

    @Indexed
    private String token;

    @Indexed
    private String username;

    public RefreshToken(String token, String username) {
        this.token = token.substring(7);
        this.username = username;
    }

    public void updateToken(String refresh) {
        this.token = refresh.substring(7);
    }
}