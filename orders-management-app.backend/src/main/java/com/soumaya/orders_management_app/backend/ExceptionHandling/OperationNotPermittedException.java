package com.soumaya.orders_management_app.backend.ExceptionHandling;

public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException(String message) {
        super(message);
    }
}
