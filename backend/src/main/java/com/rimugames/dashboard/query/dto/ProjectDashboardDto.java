package com.rimugames.dashboard.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDashboardDto {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String techStack;
    private String status;
}
