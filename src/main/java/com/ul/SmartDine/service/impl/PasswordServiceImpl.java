package com.ul.SmartDine.service.impl;

import com.ul.SmartDine.dtos.req.ForgotPasswordRequestDto;
import com.ul.SmartDine.dtos.req.OtpVerifyRequestDto;
import com.ul.SmartDine.dtos.req.ResetPasswordRequestDto;
import com.ul.SmartDine.entity.User;
import com.ul.SmartDine.exceptions.InvalidOtpException;
import com.ul.SmartDine.exceptions.PasswordMismatchException;
import com.ul.SmartDine.repository.UserRepository;
import com.ul.SmartDine.service.EmailService;
import com.ul.SmartDine.service.OtpService;
import com.ul.SmartDine.service.PasswordService;
import com.ul.SmartDine.util.KeycloakUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final KeycloakUtil keycloakUtil;
    private final EmailService emailService;

    @Override
    public void forgotPassword(ForgotPasswordRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
        otpService.sendOtp(dto.getEmail());
    }

    @Override
    public void resetPassword(ResetPasswordRequestDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
        OtpVerifyRequestDto verifyDto = OtpVerifyRequestDto.builder().email(dto.getEmail()).otpCode(dto.getOtpCode()).build();
        boolean valid = otpService.verifyOtp(verifyDto);
        if (!valid) {
            throw new InvalidOtpException();
        }
        keycloakUtil.resetPassword(user.getKeycloakId(), dto.getNewPassword());
    }
}
