package com.ul.SmartDine.api;

import com.ul.SmartDine.dtos.req.*;
import com.ul.SmartDine.dtos.resp.ApiResponseDto;
import com.ul.SmartDine.dtos.resp.AuthResponseDto;
import com.ul.SmartDine.dtos.resp.TokenRefreshResponseDto;
import com.ul.SmartDine.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * POST /api/v1/auth/signup
     * Register a new local account. Sends OTP for email verification.
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<Void>> signup(@Valid @RequestBody SignupRequestDto dto) {
        authService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success("Account created. Please verify your email with the OTP sent."));
    }

    /**
     * POST /api/v1/auth/login
     * Authenticate with email + password.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto dto) {
        AuthResponseDto response = authService.login(dto);
        return ResponseEntity.ok(ApiResponseDto.success("Login successful", response));
    }

    /**
     * POST /api/v1/auth/login/google
     * Authenticate with a Google ID token.
     */
    @PostMapping("/login/google")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> loginWithGoogle(@Valid @RequestBody GoogleLoginRequestDto dto) {
        AuthResponseDto response = authService.loginWithGoogle(dto);
        return ResponseEntity.ok(ApiResponseDto.success("Google login successful", response));
    }

    /**
     * POST /api/v1/auth/login/github
     * Authenticate with a GitHub authorization code.
     */
    @PostMapping("/login/github")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> loginWithGitHub(@Valid @RequestBody GitHubLoginRequestDto dto) {
        AuthResponseDto response = authService.loginWithGitHub(dto);
        return ResponseEntity.ok(ApiResponseDto.success("GitHub login successful", response));
    }

    /**
     * POST /api/v1/auth/refresh
     * Exchange a refresh token for a new token pair.
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDto<TokenRefreshResponseDto>> refreshToken(@Valid @RequestBody TokenRefreshRequestDto dto) {
        TokenRefreshResponseDto response = authService.refreshToken(dto);
        return ResponseEntity.ok(ApiResponseDto.success("Token refreshed", response));
    }

    /**
     * POST /api/v1/auth/logout
     * Invalidate the refresh token in Keycloak.
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout(@RequestParam String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok(ApiResponseDto.success("Logged out successfully"));
    }
}
