package com.willyoubackend.domain.user_profile.repository;

import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImageEntity, Long>, ProfileImageRepositoryCustom {
}