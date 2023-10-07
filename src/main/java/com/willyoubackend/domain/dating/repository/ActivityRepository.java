package com.willyoubackend.domain.dating.repository;

import com.willyoubackend.domain.dating.entity.Activity;
import com.willyoubackend.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByOrderByCreatedAt();
    List<Activity> findAllByUserOrderByCreatedAtDesc(UserEntity user);
}
