package com.willyoubackend.domain.user_profile.repository;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileImageRepository extends JpaRepository<ProfileImageEntity, Long> {
    List<ProfileImageEntity> findAllByUserEntity(UserEntity userEntity);
}
