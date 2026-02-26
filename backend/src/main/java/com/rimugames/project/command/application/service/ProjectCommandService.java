package com.rimugames.project.command.application.service;

import com.rimugames.exception.BusinessException;
import com.rimugames.exception.ErrorCode;
import com.rimugames.project.command.application.dto.request.ProjectRequest;
import com.rimugames.project.command.application.dto.response.ProjectResponse;
import com.rimugames.project.command.domain.aggregate.Project;
import com.rimugames.project.command.domain.repository.ProjectRepository;
import com.rimugames.user.command.domain.aggregate.User;
import com.rimugames.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectCommandService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProjectResponse createProject(String email, ProjectRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Project project = Project.builder()
                .userId(user.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .thumbnailUrl(request.getThumbnailUrl())
                .projectUrl(request.getProjectUrl())
                .repoUrl(request.getRepoUrl())
                .techStack(request.getTechStack())
                .status(request.getStatus())
                .build();

        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse updateProject(String email, Long projectId, ProjectRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.getUserId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        project.update(request.getTitle(), request.getDescription(), request.getThumbnailUrl(),
                request.getProjectUrl(), request.getRepoUrl(), request.getTechStack(), request.getStatus());

        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(String email, Long projectId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.getUserId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        projectRepository.deleteById(projectId);
    }

    public List<ProjectResponse> getMyProjects(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return projectRepository.findByUserId(user.getId()).stream()
                .map(ProjectResponse::from)
                .collect(Collectors.toList());
    }
}
