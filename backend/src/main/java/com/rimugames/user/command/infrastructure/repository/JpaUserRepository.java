package com.rimugames.user.command.infrastructure.repository;

import com.rimugames.user.command.domain.aggregate.User;
import com.rimugames.user.command.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
