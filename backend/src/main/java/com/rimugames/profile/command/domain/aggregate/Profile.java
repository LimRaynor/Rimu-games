package com.rimugames.profile.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    // Basic Info
    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String role;         // ex) "Game Developer", "2D Artist"

    @Column(columnDefinition = "TEXT")
    private String bio;          // 자기소개

    @Column(length = 255)
    private String avatarUrl;    // 프로필 이미지

    // Contact
    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String githubUrl;

    @Column(length = 255)
    private String websiteUrl;

    @Column(length = 100)
    private String discordTag;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void update(String name, String role, String bio, String avatarUrl,
                       String email, String githubUrl, String websiteUrl, String discordTag) {
        this.name = name;
        this.role = role;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.githubUrl = githubUrl;
        this.websiteUrl = websiteUrl;
        this.discordTag = discordTag;
    }
}
