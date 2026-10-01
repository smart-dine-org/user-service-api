package com.ul.SmartDine.service.impl;

import com.ul.SmartDine.config.OtpConfig;
import com.ul.SmartDine.dtos.req.OtpVerifyRequestDto;
import com.ul.SmartDine.entity.User;
import com.ul.SmartDine.exceptions.OtpExpiredException;
import com.ul.SmartDine.exceptions.OtpMaxAttemptsExceededException;
import com.ul.SmartDine.repository.UserRepository;
import com.ul.SmartDine.service.EmailService;
import com.ul.SmartDine.service.OtpService;
import com.ul.SmartDine.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private static final String OTP_KEY_PREFIX = "otp:";
    private static final String OTP_ATTEMPTS_KEY_PREFIX = "otp:attempts:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final OtpConfig.OtpProperties otpProperties;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OtpUtil otpUtil;

    @Override
    public void sendOtp(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        String otpCode = otpUtil.generate(otpProperties.getLength());
        String key = OTP_KEY_PREFIX + email;
        redisTemplate.opsForValue().set(key, otpCode, otpProperties.getExpiryMinutes(), TimeUnit.MINUTES);
        String attemptsKey = OTP_ATTEMPTS_KEY_PREFIX + email;
        redisTemplate.delete(attemptsKey);
        emailService.sendOtpEmail(email, user.getFirstName(), otpCode);
    }

    @Override
    public boolean verifyOtp(OtpVerifyRequestDto dto) {
        String key = OTP_KEY_PREFIX + dto.getEmail();
        String attemptsKey = OTP_ATTEMPTS_KEY_PREFIX + dto.getEmail();
        String storedOtp = (String) redisTemplate.opsForValue().get(key);
        if (storedOtp == null) {
            throw new OtpExpiredException(dto.getEmail());
        }
        Integer attempts = (Integer) redisTemplate.opsForValue().get(attemptsKey);
        int currentAttempts = attempts == null ? 0 : attempts;
        if (currentAttempts >= otpProperties.getMaxAttempts()) {
            redisTemplate.delete(key);
            redisTemplate.delete(attemptsKey);
            throw new OtpMaxAttemptsExceededException(dto.getEmail());
        }
        if (!storedOtp.equals(dto.getOtpCode())) {
            redisTemplate.opsForValue().set(attemptsKey, currentAttempts + 1, otpProperties.getExpiryMinutes(), TimeUnit.MINUTES);
            return false;
        }
        redisTemplate.delete(key);
        redisTemplate.delete(attemptsKey);
        return true;
    }

    @Override
    public void validateOtp(String email) {
        redisTemplate.delete(OTP_KEY_PREFIX + email);
        redisTemplate.delete(OTP_ATTEMPTS_KEY_PREFIX + email);
    }
}
