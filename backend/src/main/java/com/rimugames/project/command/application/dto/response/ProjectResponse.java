package com.rimugames.project.command.application.dto.response;

import com.rimugames.project.command.domain.aggregate.Project;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProjectResponse {

    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String projectUrl;
    private String repoUrl;
    private String techStack;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProjectResponse from(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .userId(project.getUserId())
                .title(project.getTitle())
                .description(project.getDescription())
                .thumbnailUrl(project.getThumbnailUrl())
                .projectUrl(project.getProjectUrl())
                .repoUrl(project.getRepoUrl())
                .techStack(project.getTechStack())
                .status(project.getStatus())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
