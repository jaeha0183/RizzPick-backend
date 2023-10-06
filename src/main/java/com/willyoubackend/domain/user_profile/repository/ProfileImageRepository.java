package com.willyoubackend.domain.user_profile.repository;

import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImageEntity, Long> {
}
