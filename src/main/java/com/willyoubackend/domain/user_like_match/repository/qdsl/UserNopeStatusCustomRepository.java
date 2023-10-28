package com.willyoubackend.domain.user_like_match.repository.qdsl;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_like_match.entity.UserNopeStatus;

public interface UserNopeStatusCustomRepository {
    UserNopeStatus findBySentUserAndReceivedUser(UserEntity sentUser, UserEntity receivedUser);

    Boolean existBySentUserAndReceivedUser(UserEntity sentUser, UserEntity receivedUser);
}
