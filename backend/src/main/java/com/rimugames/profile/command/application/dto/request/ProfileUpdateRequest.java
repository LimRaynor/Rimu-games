package com.rimugames.profile.command.application.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProfileUpdateRequest {

    @Size(max = 100, message = "이름은 100자 이하이어야 합니다.")
    private String name;

    @Size(max = 100, message = "역할은 100자 이하이어야 합니다.")
    private String role;

    private String bio;

    private String avatarUrl;

    private String email;

    private String githubUrl;

    private String websiteUrl;

    private String discordTag;
}
