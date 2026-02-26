package com.rimugames.auth.command.application.service;

import com.rimugames.auth.command.application.dto.request.LoginRequest;
import com.rimugames.auth.command.application.dto.request.SignupRequest;
import com.rimugames.auth.command.application.dto.response.SignupResponse;
import com.rimugames.auth.command.application.dto.response.TokenResponse;
import com.rimugames.auth.command.domain.aggregate.RefreshToken;
import com.rimugames.auth.command.domain.repository.RefreshTokenRepository;
import com.rimugames.exception.BusinessException;
import com.rimugames.exception.ErrorCode;
import com.rimugames.security.JwtTokenProvider;
import com.rimugames.user.command.domain.aggregate.User;
import com.rimugames.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthCommandService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        User savedUser = userRepository.save(user);

        return new SignupResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getNickname());
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRole().name());

        RefreshToken tokenEntity = RefreshToken.builder()
                .userEmail(user.getEmail())
                .token(refreshToken)
                .expiryDate(new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshExpiration()))
                .build();

        refreshTokenRepository.save(tokenEntity);

        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Transactional
    public TokenResponse refreshToken(String provideRefreshToken) {
        jwtTokenProvider.validateToken(provideRefreshToken);

        String userEmail = jwtTokenProvider.getUserEmailFromJWTToken(provideRefreshToken);

        RefreshToken storedToken = refreshTokenRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!storedToken.getToken().equals(provideRefreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        if (storedToken.getExpiryDate().before(new Date())) {
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRole().name());

        RefreshToken tokenEntity = RefreshToken.builder()
                .userEmail(userEmail)
                .token(refreshToken)
                .expiryDate(new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshExpiration()))
                .build();

        refreshTokenRepository.save(tokenEntity);

        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Transactional
    public void logout(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        String userEmail = jwtTokenProvider.getUserEmailFromJWTToken(refreshToken);
        refreshTokenRepository.deleteByUserEmail(userEmail);
    }
}
