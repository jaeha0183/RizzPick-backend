package com.willyoubackend.domain.dating.repository.qdsl;

import com.willyoubackend.domain.dating.entity.Activity;
import com.willyoubackend.domain.user.entity.UserEntity;

import java.util.List;

public interface ActivityCustomRepository {
    List<Activity> findAllByOrderByCreatedAt();

    List<Activity> findAllByUserOrderByCreatedAtDesc(UserEntity user);
}
