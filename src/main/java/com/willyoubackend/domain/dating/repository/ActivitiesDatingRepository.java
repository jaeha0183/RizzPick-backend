package com.willyoubackend.domain.dating.repository;

import com.willyoubackend.domain.dating.entity.ActivitiesDating;
import com.willyoubackend.domain.dating.entity.Dating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivitiesDatingRepository extends JpaRepository<ActivitiesDating, Long> {
    List<ActivitiesDating> findAllActivitiesDatingByDating(Dating dating);
}
