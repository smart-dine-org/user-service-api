package com.ul.SmartDine.exceptions;

import org.springframework.http.HttpStatus;

public class OtpMaxAttemptsExceededException extends BaseException {

    public OtpMaxAttemptsExceededException(String email) {
        super("Maximum OTP verification attempts exceeded for: " + email, HttpStatus.TOO_MANY_REQUESTS, "OTP_MAX_ATTEMPTS_EXCEEDED");
    }
}