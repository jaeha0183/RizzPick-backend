package com.willyoubackend.domain.user.repository;

import com.willyoubackend.domain.user.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUsername(String username);

    boolean existsByToken(String refresh);
}
