package com.project.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.auth.entity.RefreshTokenEntity;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
	Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);
}
