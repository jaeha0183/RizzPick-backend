package com.willyoubackend.domain.dating.repository;

import com.willyoubackend.domain.dating.entity.Dating;
import com.willyoubackend.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DatingRepository extends JpaRepository<Dating, Long> {
    List<Dating> findAllByOrderByCreatedAt();
    List<Dating> findAllByUserOrderByCreatedAt(UserEntity user);
    List<Dating> findAllByLocationOrderByCreatedAt(String location);
}
