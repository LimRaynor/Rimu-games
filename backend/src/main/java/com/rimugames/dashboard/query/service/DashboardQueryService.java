package com.rimugames.dashboard.query.service;

import com.rimugames.dashboard.query.dto.ProjectDashboardDto;
import com.rimugames.dashboard.query.dto.TeamMemberDto;
import com.rimugames.dashboard.query.mapper.DashboardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardQueryService {

    private final DashboardMapper dashboardMapper;

    public List<TeamMemberDto> getTeamMembers() {
        return dashboardMapper.findAllTeamMembers();
    }

    public List<ProjectDashboardDto> getAllProjects() {
        return dashboardMapper.findAllProjects();
    }
}
