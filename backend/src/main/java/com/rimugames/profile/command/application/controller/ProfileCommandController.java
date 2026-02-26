package com.rimugames.profile.command.application.controller;

import com.rimugames.common.dto.ApiResponse;
import com.rimugames.profile.command.application.dto.request.ProfileUpdateRequest;
import com.rimugames.profile.command.application.dto.response.ProfileResponse;
import com.rimugames.profile.command.application.service.ProfileCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileCommandController {

    private final ProfileCommandService profileCommandService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        ProfileResponse profile = profileCommandService.getMyProfile(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProfileUpdateRequest request) {
        ProfileResponse profile = profileCommandService.upsertProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
}
