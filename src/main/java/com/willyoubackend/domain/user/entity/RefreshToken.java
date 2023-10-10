package com.willyoubackend.domain.user.entity;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash("RefreshToken")
public class RefreshToken {
    @Id
    private String id;

    private String token;
    private String username;

    public RefreshToken(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public void updateToken(String refresh) {
        this.token = refresh;
    }

    // getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}