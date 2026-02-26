package com.rimugames.auth.command.infrastructure.repository;

import com.rimugames.auth.command.domain.aggregate.RefreshToken;
import com.rimugames.auth.command.domain.repository.RefreshTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshToken, String>, RefreshTokenRepository {
    Optional<RefreshToken> findByUserEmail(String userEmail);
    void deleteByUserEmail(String userEmail);
}
