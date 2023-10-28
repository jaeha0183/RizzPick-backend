package com.willyoubackend.domain.dating.repository;

import com.willyoubackend.domain.dating.entity.ActivitiesDating;
import com.willyoubackend.domain.dating.entity.Activity;
import com.willyoubackend.domain.dating.entity.Dating;
import com.willyoubackend.domain.dating.repository.qdsl.ActivitiesDatingCustomRepository;
import com.willyoubackend.domain.dating.repository.qdsl.ActivityCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivitiesDatingRepository extends JpaRepository<ActivitiesDating, Long>, ActivitiesDatingCustomRepository {
}