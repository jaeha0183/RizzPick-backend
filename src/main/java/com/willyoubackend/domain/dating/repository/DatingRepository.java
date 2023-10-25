package com.willyoubackend.domain.dating.repository;

import com.willyoubackend.domain.dating.entity.Dating;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DatingRepository extends JpaRepository<Dating, Long>, DatingCustomRepository {
}