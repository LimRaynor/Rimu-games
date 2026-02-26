package com.rimugames.project.command.application.controller;

import com.rimugames.common.dto.ApiResponse;
import com.rimugames.project.command.application.dto.request.ProjectRequest;
import com.rimugames.project.command.application.dto.response.ProjectResponse;
import com.rimugames.project.command.application.service.ProjectCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectCommandController {

    private final ProjectCommandService projectCommandService;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getMyProjects(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<ProjectResponse> projects = projectCommandService.getMyProjects(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProjectRequest request) {
        ProjectResponse project = projectCommandService.createProject(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(project));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequest request) {
        ProjectResponse project = projectCommandService.updateProject(userDetails.getUsername(), projectId, request);
        return ResponseEntity.ok(ApiResponse.success(project));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long projectId) {
        projectCommandService.deleteProject(userDetails.getUsername(), projectId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
