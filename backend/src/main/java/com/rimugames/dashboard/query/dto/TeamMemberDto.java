package com.rimugames.dashboard.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberDto {
    private Long userId;
    private String nickname;
    private String name;
    private String role;
    private String bio;
    private String avatarUrl;
    private int projectCount;
    // Contact
    private String email;
    private String githubUrl;
    private String websiteUrl;
    private String discordTag;
}
