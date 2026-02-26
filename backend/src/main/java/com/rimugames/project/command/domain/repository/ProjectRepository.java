package com.rimugames.project.command.domain.repository;

import com.rimugames.project.command.domain.aggregate.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project save(Project project);
    Optional<Project> findById(Long id);
    List<Project> findByUserId(Long userId);
    void deleteById(Long id);
}
