package com.ul.SmartDine.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends BaseException {

    public DuplicateEmailException(String email) {
        super("Email already registered: " + email, HttpStatus.CONFLICT, "DUPLICATE_EMAIL");
    }
}