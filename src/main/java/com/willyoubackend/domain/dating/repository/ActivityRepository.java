package com.willyoubackend.domain.dating.repository;

import com.willyoubackend.domain.dating.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long>, ActivityCustomRepository {
}