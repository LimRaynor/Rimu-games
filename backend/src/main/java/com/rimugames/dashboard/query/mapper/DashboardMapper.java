package com.rimugames.dashboard.query.mapper;

import com.rimugames.dashboard.query.dto.ProjectDashboardDto;
import com.rimugames.dashboard.query.dto.TeamMemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashboardMapper {
    List<TeamMemberDto> findAllTeamMembers();
    List<ProjectDashboardDto> findAllProjects();
}
