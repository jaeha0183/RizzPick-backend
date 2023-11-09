package com.willyoubackend.domain.user.repository;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.GenderEnum;
import io.sentry.protocol.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryCustom {
    List<UserEntity> findByUserProfileEntity_LocationAndIdNot(String location, Long id);

    List<UserEntity> findByUserProfileEntity_LocationAndUserProfileEntity_GenderNotAndIdNot(String location, GenderEnum gender, Long id);

    List<UserEntity> findAllUserEntityExceptAdminAndNonActive();
}