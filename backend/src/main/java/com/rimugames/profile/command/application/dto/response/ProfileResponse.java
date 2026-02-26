package com.rimugames.profile.command.application.dto.response;

import com.rimugames.profile.command.domain.aggregate.Profile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProfileResponse {

    private Long id;
    private Long userId;
    private String name;
    private String role;
    private String bio;
    private String avatarUrl;
    private String email;
    private String githubUrl;
    private String websiteUrl;
    private String discordTag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProfileResponse from(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .name(profile.getName())
                .role(profile.getRole())
                .bio(profile.getBio())
                .avatarUrl(profile.getAvatarUrl())
                .email(profile.getEmail())
                .githubUrl(profile.getGithubUrl())
                .websiteUrl(profile.getWebsiteUrl())
                .discordTag(profile.getDiscordTag())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
