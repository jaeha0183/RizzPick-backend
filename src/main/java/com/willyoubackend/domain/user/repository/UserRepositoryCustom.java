package com.willyoubackend.domain.user.repository;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.GenderEnum;
import com.willyoubackend.domain.user_profile.entity.LocationEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryCustom {
    List<UserEntity> findByUserProfileEntity_LocationAndIdNot(LocationEnum location, Long id);

    List<UserEntity> findByUserProfileEntity_LocationAndUserProfileEntity_GenderNotAndIdNot(LocationEnum location, GenderEnum gender, Long id);
}
