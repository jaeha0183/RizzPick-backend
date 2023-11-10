package com.willyoubackend.domain.dating.repository;

import com.willyoubackend.domain.dating.entity.ActivitiesDating;
import com.willyoubackend.domain.dating.repository.qdsl.ActivitiesDatingCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivitiesDatingRepository extends JpaRepository<ActivitiesDating, Long>, ActivitiesDatingCustomRepository {
}