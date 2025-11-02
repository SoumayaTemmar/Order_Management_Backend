package com.soumaya.orders_management_app.backend.ExceptionHandling;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }
}
