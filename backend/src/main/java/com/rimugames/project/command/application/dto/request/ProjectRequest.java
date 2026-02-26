package com.rimugames.project.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProjectRequest {

    @NotBlank(message = "프로젝트 제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자 이하이어야 합니다.")
    private String title;

    private String description;

    private String thumbnailUrl;

    private String projectUrl;

    private String repoUrl;

    private String techStack;

    private String status;
}
