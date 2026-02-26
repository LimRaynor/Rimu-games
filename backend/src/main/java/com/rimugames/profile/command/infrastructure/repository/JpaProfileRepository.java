package com.rimugames.profile.command.infrastructure.repository;

import com.rimugames.profile.command.domain.aggregate.Profile;
import com.rimugames.profile.command.domain.repository.ProfileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaProfileRepository extends JpaRepository<Profile, Long>, ProfileRepository {
    Optional<Profile> findByUserId(Long userId);
}
