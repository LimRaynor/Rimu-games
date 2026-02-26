package com.rimugames.profile.command.application.service;

import com.rimugames.exception.BusinessException;
import com.rimugames.exception.ErrorCode;
import com.rimugames.profile.command.application.dto.request.ProfileUpdateRequest;
import com.rimugames.profile.command.application.dto.response.ProfileResponse;
import com.rimugames.profile.command.domain.aggregate.Profile;
import com.rimugames.profile.command.domain.repository.ProfileRepository;
import com.rimugames.user.command.domain.aggregate.User;
import com.rimugames.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileCommandService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProfileResponse upsertProfile(String email, ProfileUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElse(null);

        if (profile == null) {
            profile = Profile.builder()
                    .userId(user.getId())
                    .name(request.getName())
                    .role(request.getRole())
                    .bio(request.getBio())
                    .avatarUrl(request.getAvatarUrl())
                    .email(request.getEmail())
                    .githubUrl(request.getGithubUrl())
                    .websiteUrl(request.getWebsiteUrl())
                    .discordTag(request.getDiscordTag())
                    .build();
        } else {
            profile.update(
                    request.getName(), request.getRole(), request.getBio(), request.getAvatarUrl(),
                    request.getEmail(), request.getGithubUrl(), request.getWebsiteUrl(), request.getDiscordTag()
            );
        }

        Profile savedProfile = profileRepository.save(profile);
        return ProfileResponse.from(savedProfile);
    }

    public ProfileResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        return ProfileResponse.from(profile);
    }
}
