package com.willyoubackend.domain.user_profile.repository;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepositoryCustom {
    UserProfileEntity findByUserEntity(UserEntity user);
}
