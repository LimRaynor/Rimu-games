package com.rimugames.auth.command.domain.repository;

import com.rimugames.auth.command.domain.aggregate.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByUserEmail(String userEmail);
    void deleteByUserEmail(String userEmail);
}
