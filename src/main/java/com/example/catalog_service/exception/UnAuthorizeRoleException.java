package com.example.catalog_service.exception;

public class UnAuthorizeRoleException extends RuntimeException {
    public UnAuthorizeRoleException(String message) {
        super(message);
    }
}
