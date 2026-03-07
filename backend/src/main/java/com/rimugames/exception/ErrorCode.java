package com.rimugames.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "BAD_REQUEST_001", "잘못된 입력 값입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "BAD_REQUEST_002", "이미 사용 중인 이메일입니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "BAD_REQUEST_003", "이메일 또는 비밀번호가 올바르지 않습니다."),

    // 401
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_002", "만료된 토큰입니다."),

    // 403
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "FORBIDDEN_001", "접근 권한이 없습니다."),

    // 404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_001", "사용자를 찾을 수 없습니다."),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_002", "프로필을 찾을 수 없습니다."),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_003", "프로젝트를 찾을 수 없습니다."),
    TEAM_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_004", "팀원을 찾을 수 없습니다."),

    // 500
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_ERROR_001", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
