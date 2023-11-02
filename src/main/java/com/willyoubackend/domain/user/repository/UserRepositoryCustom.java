package com.willyoubackend.domain.user.repository;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.GenderEnum;
import com.willyoubackend.domain.user_profile.entity.LocationEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryCustom {
    List<UserEntity> findByUserProfileEntity_LocationAndIdNot(String location, Long id);

    List<UserEntity> findByUserProfileEntity_LocationAndUserProfileEntity_GenderNotAndIdNot(String location, GenderEnum gender, Long id);
}