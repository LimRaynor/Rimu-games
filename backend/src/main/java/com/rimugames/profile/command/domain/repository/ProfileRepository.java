package com.rimugames.profile.command.domain.repository;

import com.rimugames.profile.command.domain.aggregate.Profile;

import java.util.Optional;

public interface ProfileRepository {
    Profile save(Profile profile);
    Optional<Profile> findByUserId(Long userId);
    Optional<Profile> findById(Long id);
}
