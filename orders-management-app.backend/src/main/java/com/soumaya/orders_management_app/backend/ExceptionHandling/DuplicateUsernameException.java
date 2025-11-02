package com.soumaya.orders_management_app.backend.ExceptionHandling;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
