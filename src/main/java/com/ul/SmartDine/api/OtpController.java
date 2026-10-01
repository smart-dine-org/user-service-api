package com.ul.SmartDine.api;

import com.ul.SmartDine.dtos.req.OtpVerifyRequestDto;
import com.ul.SmartDine.dtos.resp.ApiResponseDto;
import com.ul.SmartDine.entity.User;
import com.ul.SmartDine.entity.enums.UserStatus;
import com.ul.SmartDine.repository.UserRepository;
import com.ul.SmartDine.service.EmailService;
import com.ul.SmartDine.service.OtpService;
import com.ul.SmartDine.util.KeycloakUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;
    private final UserRepository userRepository;
    private final KeycloakUtil keyCloakUtil;
    private final EmailService emailService;

    /**
     * POST /api/v1/otp/send?email=
     * Resend an OTP to the given email address.
     */
    @PostMapping("/send")
    public ResponseEntity<ApiResponseDto<Void>> sendOtp(@RequestParam String email) {
        otpService.sendOtp(email);
        return ResponseEntity.ok(ApiResponseDto.success("OTP sent to " + email));
    }

    /**
     * POST /api/v1/otp/verify
     * Verify the OTP and activate the user account.
     */
    @PostMapping("/verify")
    public ResponseEntity<ApiResponseDto<Void>> verifyOtp(@Valid @RequestBody OtpVerifyRequestDto dto) {
        boolean valid = otpService.verifyOtp(dto);
        if (!valid) {
            return ResponseEntity.badRequest().body(ApiResponseDto.<Void>builder().message("Invalid OTP. Please try again.").build());
        }

        // Activate the user account and mark email verified in Keycloak
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        keyCloakUtil.markEmailVerified(user.getKeycloakId());
        emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());

        return ResponseEntity.ok(ApiResponseDto.success("Email verified successfully. Welcome to SmartDine!"));
    }
}