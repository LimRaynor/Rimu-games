package com.rimugames.dashboard.query.controller;

import com.rimugames.common.dto.ApiResponse;
import com.rimugames.dashboard.query.dto.ProjectDashboardDto;
import com.rimugames.dashboard.query.dto.TeamMemberDto;
import com.rimugames.dashboard.query.service.DashboardQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardQueryController {

    private final DashboardQueryService dashboardQueryService;

    @GetMapping("/team")
    public ResponseEntity<ApiResponse<List<TeamMemberDto>>> getTeamMembers() {
        return ResponseEntity.ok(ApiResponse.success(dashboardQueryService.getTeamMembers()));
    }

    @GetMapping("/projects")
    public ResponseEntity<ApiResponse<List<ProjectDashboardDto>>> getProjects() {
        return ResponseEntity.ok(ApiResponse.success(dashboardQueryService.getAllProjects()));
    }
}
