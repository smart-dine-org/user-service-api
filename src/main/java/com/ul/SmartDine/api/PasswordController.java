package com.ul.SmartDine.api;

import com.ul.SmartDine.dtos.req.ForgotPasswordRequestDto;
import com.ul.SmartDine.dtos.req.ResetPasswordRequestDto;
import com.ul.SmartDine.dtos.resp.ApiResponseDto;
import com.ul.SmartDine.service.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    /**
     * POST /api/v1/password/forgot
     * Sends a reset OTP to the user's email.
     */
    @PostMapping("/forgot")
    public ResponseEntity<ApiResponseDto<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto dto) {
        passwordService.forgotPassword(dto);
        return ResponseEntity.ok(ApiResponseDto.success("Password reset OTP sent to " + dto.getEmail()));
    }

    /**
     * POST /api/v1/password/reset
     * Resets the password after OTP verification.
     */
    @PostMapping("/reset")
    public ResponseEntity<ApiResponseDto<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto dto) {
        passwordService.resetPassword(dto);
        return ResponseEntity.ok(ApiResponseDto.success("Password reset successfully. You can now log in."));
    }
}