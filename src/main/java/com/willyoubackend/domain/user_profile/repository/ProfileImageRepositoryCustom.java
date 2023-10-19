package com.willyoubackend.domain.user_profile.repository;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileImageRepositoryCustom {
    List<ProfileImageEntity> findAllByUserEntity(UserEntity userEntity);
}