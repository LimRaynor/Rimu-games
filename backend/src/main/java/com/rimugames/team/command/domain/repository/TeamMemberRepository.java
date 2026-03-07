package com.rimugames.team.command.domain.repository;

import com.rimugames.team.command.domain.aggregate.TeamMember;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository {
    List<TeamMember> findAllByOrderBySortOrderAsc();
    Optional<TeamMember> findById(Long id);
    TeamMember save(TeamMember teamMember);
    void delete(TeamMember teamMember);
}
