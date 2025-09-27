// src/main/java/com/hanguyen/hospital_management/service/AuthService.java
package com.hanguyen.hospital_management.service;

import com.hanguyen.hospital_management.dto.*;
import com.hanguyen.hospital_management.entity.RefreshToken;
import com.hanguyen.hospital_management.entity.StaffAccount;
import com.hanguyen.hospital_management.repository.RefreshTokenRepository;
import com.hanguyen.hospital_management.repository.StaffAccountRepository;
import com.hanguyen.hospital_management.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StaffAccountRepository staffAccountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        StaffAccount user = staffAccountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = createRefreshToken(user);

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .user(userDto)
                .build();
    }

    public AuthResponse refresh(RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().before(new Date())) {
            throw new IllegalArgumentException("Refresh token expired or revoked");
        }

        StaffAccount user = refreshToken.getStaffAccount();
        String newAccessToken = jwtService.generateAccessToken(user);
        RefreshToken newRefreshToken = createRefreshToken(user);  // Rotate refresh token
        refreshTokenRepository.delete(refreshToken);  // Delete old

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

    public void changePassword(ChangePasswordRequest request, StaffAccount user) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        staffAccountRepository.save(user);
    }

    public void logout(StaffAccount user) {
        refreshTokenRepository.deleteByStaffAccountId(user.getId());
    }

    private RefreshToken createRefreshToken(StaffAccount user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setStaffAccount(user);
        refreshToken.setToken(jwtService.generateRefreshToken(user));
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + jwtService.getRefreshExpiration()));  // Use getter here
        return refreshTokenRepository.save(refreshToken);
    }
}