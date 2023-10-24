package com.willyoubackend.domain.sse.repository;

import com.willyoubackend.domain.sse.entity.Alert;
import com.willyoubackend.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByReceiver(UserEntity user);
}
