package com.rimugames.team.command.application.service;

import com.rimugames.exception.BusinessException;
import com.rimugames.exception.ErrorCode;
import com.rimugames.team.command.application.dto.request.TeamMemberRequest;
import com.rimugames.team.command.application.dto.response.TeamMemberResponse;
import com.rimugames.team.command.domain.aggregate.TeamMember;
import com.rimugames.team.command.domain.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMemberCommandService {

    private final TeamMemberRepository teamMemberRepository;

    public List<TeamMemberResponse> getAll() {
        return teamMemberRepository.findAllByOrderBySortOrderAsc()
                .stream()
                .map(TeamMemberResponse::from)
                .toList();
    }

    @Transactional
    public TeamMemberResponse create(TeamMemberRequest request) {
        TeamMember member = TeamMember.builder()
                .name(request.getName())
                .role(request.getRole())
                .sortOrder(request.getSortOrder())
                .build();
        return TeamMemberResponse.from(teamMemberRepository.save(member));
    }

    @Transactional
    public TeamMemberResponse update(Long id, TeamMemberRequest request) {
        TeamMember member = teamMemberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_MEMBER_NOT_FOUND));
        member.update(request.getName(), request.getRole(), request.getSortOrder());
        return TeamMemberResponse.from(teamMemberRepository.save(member));
    }

    @Transactional
    public void delete(Long id) {
        TeamMember member = teamMemberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_MEMBER_NOT_FOUND));
        teamMemberRepository.delete(member);
    }
}
