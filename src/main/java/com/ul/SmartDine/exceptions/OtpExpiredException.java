package com.ul.SmartDine.exceptions;

import org.springframework.http.HttpStatus;

public class OtpExpiredException extends BaseException {

    public OtpExpiredException(String email) {
        super("OTP has expired for: " + email, HttpStatus.BAD_REQUEST, "OTP_EXPIRED");
    }
}