package com.willyoubackend.domain.user.repository;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.GenderEnum;
import com.willyoubackend.domain.user_profile.entity.LocationEnum;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByKakaoId(Long kakaoId);

    @EntityGraph(attributePaths = "profileImages")
    List<UserEntity> findByUserProfileEntity_LocationAndIdNot(LocationEnum location, Long id);

    @EntityGraph(attributePaths = "profileImages")
    List<UserEntity> findByUserProfileEntity_LocationAndUserProfileEntity_GenderNotAndIdNot(LocationEnum location, GenderEnum gender, Long id);
}
