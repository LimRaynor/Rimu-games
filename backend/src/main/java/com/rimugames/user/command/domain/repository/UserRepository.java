package com.rimugames.user.command.domain.repository;

import com.rimugames.user.command.domain.aggregate.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findById(Long id);
}
