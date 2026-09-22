package com.ul.SmartDine.exceptions;

import org.springframework.http.HttpStatus;

public class KeyCloakIntegrationException extends BaseException {

    public KeyCloakIntegrationException(String detail) {
        super("Keycloak integration error: " + detail, HttpStatus.BAD_GATEWAY, "KEYCLOAK_ERROR");
    }
}