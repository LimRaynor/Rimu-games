package com.rimugames.project.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String thumbnailUrl;

    @Column(length = 255)
    private String projectUrl;

    @Column(length = 255)
    private String repoUrl;

    @Column(length = 200)
    private String techStack;   // ex) "Unity, C#, Photoshop"

    @Column(length = 20)
    private String status;      // ex) "완성", "진행중", "기획중"

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void update(String title, String description, String thumbnailUrl,
                       String projectUrl, String repoUrl, String techStack, String status) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.projectUrl = projectUrl;
        this.repoUrl = repoUrl;
        this.techStack = techStack;
        this.status = status;
    }
}
