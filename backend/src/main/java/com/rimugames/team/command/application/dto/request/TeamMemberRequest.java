package com.rimugames.team.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TeamMemberRequest {

    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 100, message = "이름은 100자 이하이어야 합니다.")
    private String name;

    @Size(max = 100, message = "역할은 100자 이하이어야 합니다.")
    private String role;

    private int sortOrder;
}
