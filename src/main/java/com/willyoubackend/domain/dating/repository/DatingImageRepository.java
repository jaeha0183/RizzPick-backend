package com.willyoubackend.domain.dating.repository;

import com.willyoubackend.domain.dating.entity.DatingImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatingImageRepository extends JpaRepository<DatingImage, Long> {
}
