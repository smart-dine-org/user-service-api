package com.ul.SmartDine.exceptions;

import org.springframework.http.HttpStatus;

public class AccountSuspendedException extends BaseException {

    public AccountSuspendedException(String email) {
        super("Account suspended: " + email, HttpStatus.FORBIDDEN, "ACCOUNT_SUSPENDED");
    }
}