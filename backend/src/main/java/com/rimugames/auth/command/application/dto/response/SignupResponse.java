package com.rimugames.auth.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponse {
    private Long id;
    private String email;
    private String nickname;
}
