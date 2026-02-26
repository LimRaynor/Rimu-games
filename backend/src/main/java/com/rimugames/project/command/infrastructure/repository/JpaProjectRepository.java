package com.rimugames.project.command.infrastructure.repository;

import com.rimugames.project.command.domain.aggregate.Project;
import com.rimugames.project.command.domain.repository.ProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaProjectRepository extends JpaRepository<Project, Long>, ProjectRepository {
    List<Project> findByUserId(Long userId);
}
