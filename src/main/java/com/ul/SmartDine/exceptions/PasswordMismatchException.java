package com.ul.SmartDine.exceptions;

import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends BaseException {

    public PasswordMismatchException() {
        super("Password and confirm password do not match", HttpStatus.BAD_REQUEST, "PASSWORD_MISMATCH");
    }
}