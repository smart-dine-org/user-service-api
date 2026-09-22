package com.ul.SmartDine.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidOtpException extends BaseException {

    public InvalidOtpException() {
        super("Invalid OTP code provided", HttpStatus.BAD_REQUEST, "INVALID_OTP");
    }
}