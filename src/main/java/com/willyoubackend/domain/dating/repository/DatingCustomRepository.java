package com.willyoubackend.domain.dating.repository;

import com.willyoubackend.domain.dating.entity.Dating;
import com.willyoubackend.domain.user.entity.UserEntity;

import java.util.List;

public interface DatingCustomRepository {
    List<Dating> findAllByOrderByCreatedAt();

    List<Dating> findAllByUserOrderByCreatedAt(UserEntity user);

    List<Dating> findAllByLocationOrderByCreatedAt(String location);
}
