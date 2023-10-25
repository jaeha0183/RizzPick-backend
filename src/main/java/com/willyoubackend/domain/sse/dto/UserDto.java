package com.willyoubackend.domain.sse.dto;

import com.willyoubackend.domain.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {
    private Long id;

    public UserDto(UserEntity user) {
        this.id = user.getId();
    }
}
