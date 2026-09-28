package com.ul.SmartDine.service;

import com.ul.SmartDine.dtos.req.OtpVerifyRequestDto;

public interface OtpService {
    void sendOtp(String email);
    void verifyOtp(OtpVerifyRequestDto dto);
    void validateOtp(String email);
}
