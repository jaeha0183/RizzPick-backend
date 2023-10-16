package com.willyoubackend.domain.user.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "Refresh Token", timeToLive = 3600)
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

    // getters and setters
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
}