package com.soumaya.orders_management_app.backend.ExceptionHandling;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ExceptionResponse> duplicateUsernameException(
            DuplicateUsernameException ex
    ){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.DUPLICATE_USERNAME.getCode())
                        .businessErrorDescription(BusinessErrorCodes.DUPLICATE_USERNAME.getDescription())
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> operationNotPermittedException(
            OperationNotPermittedException ex
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.OPERATION_NOT_PERMITTED.getCode())
                        .businessErrorDescription(BusinessErrorCodes.OPERATION_NOT_PERMITTED.getDescription())
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ExceptionResponse> FileStorageException(
            FileStorageException ex
    ){
        return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.FILE_STORAGE.getCode())
                        .businessErrorDescription(BusinessErrorCodes.FILE_STORAGE.getDescription())
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> entityNotFound(
            EntityNotFoundException ex
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(308)
                        .businessErrorDescription("entité non trouvée")
                        .error(ex.getMessage())
                        .build());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> IllegalArgumentException(
            IllegalArgumentException ex
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(309)
                        .businessErrorDescription("argument non valide")
                        .error(ex.getMessage())
                        .build());
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> badCredentials(
            BadCredentialsException ex
    ){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.BAD_CREDENTIALS.getCode())
                        .businessErrorDescription(BusinessErrorCodes.BAD_CREDENTIALS.getDescription())
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> illegalStateException(
            IllegalStateException ex
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(312)
                        .businessErrorDescription("Etat non valide")
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> accessDeniedException(
            AccessDeniedException ex
    ){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription(BusinessErrorCodes.ACCESS_DENIED.getDescription())
                        .businessErrorCode(BusinessErrorCodes.ACCESS_DENIED.getCode())
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> lockedException(
            LockedException ex
    ){
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.ACCOUNT_LOCKED.getCode())
                        .businessErrorDescription(BusinessErrorCodes.ACCOUNT_LOCKED.getDescription())
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> disabledException(
            DisabledException ex
    ){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.ACCOUNT_DISABLED.getCode())
                        .businessErrorDescription(BusinessErrorCodes.ACCOUNT_DISABLED.getDescription())
                        .error(ex.getMessage())
                        .build());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> validationException(
            MethodArgumentNotValidException ex
    ){
        Set<String> errors = new HashSet<>();
        ex.getBindingResult().getAllErrors()
                .forEach(er-> {
                    var message= er.getDefaultMessage();
                    errors.add(message);
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .validationErrors(errors)
                        .build());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(
            Exception exception
    ){
        log.error("unexpected Error occured: ", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("error interne, veuillez contacter le admin")
                        .error(exception.getMessage())
                        .build());
    }








}
