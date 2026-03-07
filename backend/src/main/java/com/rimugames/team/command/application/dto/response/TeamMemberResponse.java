package com.rimugames.team.command.application.dto.response;

import com.rimugames.team.command.domain.aggregate.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamMemberResponse {

    private Long id;
    private String name;
    private String role;
    private int sortOrder;

    public static TeamMemberResponse from(TeamMember tm) {
        return new TeamMemberResponse(tm.getId(), tm.getName(), tm.getRole(), tm.getSortOrder());
    }
}
