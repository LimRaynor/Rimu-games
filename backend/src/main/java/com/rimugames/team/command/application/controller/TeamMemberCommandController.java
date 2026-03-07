package com.rimugames.team.command.application.controller;

import com.rimugames.common.dto.ApiResponse;
import com.rimugames.team.command.application.dto.request.TeamMemberRequest;
import com.rimugames.team.command.application.dto.response.TeamMemberResponse;
import com.rimugames.team.command.application.service.TeamMemberCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team-members")
@RequiredArgsConstructor
public class TeamMemberCommandController {

    private final TeamMemberCommandService teamMemberCommandService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeamMemberResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(teamMemberCommandService.getAll()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TeamMemberResponse>> create(@Valid @RequestBody TeamMemberRequest request) {
        return ResponseEntity.ok(ApiResponse.success(teamMemberCommandService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamMemberResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody TeamMemberRequest request) {
        return ResponseEntity.ok(ApiResponse.success(teamMemberCommandService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        teamMemberCommandService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
