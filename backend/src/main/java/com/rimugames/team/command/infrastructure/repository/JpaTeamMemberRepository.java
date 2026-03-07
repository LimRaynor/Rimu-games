package com.rimugames.team.command.infrastructure.repository;

import com.rimugames.team.command.domain.aggregate.TeamMember;
import com.rimugames.team.command.domain.repository.TeamMemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTeamMemberRepository extends JpaRepository<TeamMember, Long>, TeamMemberRepository {
}
