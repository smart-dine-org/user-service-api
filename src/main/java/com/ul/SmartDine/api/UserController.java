package com.ul.SmartDine.api;

import com.ul.SmartDine.dtos.req.UpdateProfileRequestDto;
import com.ul.SmartDine.dtos.resp.ApiResponseDto;
import com.ul.SmartDine.dtos.resp.UserResponseDto;
import com.ul.SmartDine.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * GET /api/v1/users/me
     * Returns the authenticated user's profile.
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getProfile(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = extractUserId(jwt);
        UserResponseDto profile = userService.getProfile(userId);
        return ResponseEntity.ok(ApiResponseDto.success("Profile retrieved", profile));
    }

    /**
     * PUT /api/v1/users/me
     * Updates the authenticated user's profile.
     */
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateProfile(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateProfileRequestDto dto) {
        UUID userId = extractUserId(jwt);
        UserResponseDto updated = userService.updateProfile(userId, dto);
        return ResponseEntity.ok(ApiResponseDto.success("Profile updated", updated));
    }

    /**
     * DELETE /api/v1/users/me
     * Permanently deletes the authenticated user's account.
     */
    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<Void>> deleteAccount(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = extractUserId(jwt);
        userService.deleteAccount(userId);
        return ResponseEntity.ok(ApiResponseDto.success("Account deleted successfully"));
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private UUID extractUserId(Jwt jwt) {
        // Assumes the app stores the DB UUID in the "sub" claim or a custom "userId" claim.
        // Adjust the claim name to match your Keycloak token mapper.
        String sub = jwt.getClaimAsString("sub");
        return UUID.fromString(sub);
    }
}