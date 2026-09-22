package com.ul.SmartDine.exceptions;

import org.springframework.http.HttpStatus;

public class EmailNotVerifiedException extends BaseException {

    public EmailNotVerifiedException(String email) {
        super("Email not verified for account: " + email, HttpStatus.FORBIDDEN, "EMAIL_NOT_VERIFIED");
    }
}