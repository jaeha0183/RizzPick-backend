package com.willyoubackend.domain.user_like_match.repository.qdsl;

import com.willyoubackend.domain.user.entity.UserEntity;

public interface UserMatchStatusCustomRepository {
    Boolean existByUserOneAndUserTwo(UserEntity sentUser, UserEntity receivedUser);

    Boolean existByUserTwoAndUserOne(UserEntity sentUser, UserEntity receivedUser);
}
