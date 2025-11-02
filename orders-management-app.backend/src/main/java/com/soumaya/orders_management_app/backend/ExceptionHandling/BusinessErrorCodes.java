package com.soumaya.orders_management_app.backend.ExceptionHandling;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No Code"),
    ACCOUNT_LOCKED(300,HttpStatus.FORBIDDEN,"Le Compte Utilisateur est Verrouillé, Veuillez Contacter le Admin"),
    ACCOUNT_DISABLED(301,HttpStatus.FORBIDDEN,"Le Compte Utilisateur est désactivé, Veuillez Contacter le Admin"),
    BAD_CREDENTIALS(302, HttpStatus.UNAUTHORIZED,"Nom d'utilisateur ou Mot de Passe Incorrecte"),
    ACCESS_DENIED(304,HttpStatus.FORBIDDEN, "vous n'avez pas suffisamment d'autorisations pour effectuer cette action "),
    DUPLICATE_USERNAME(305,HttpStatus.CONFLICT,"Utilisateur dupliqué" ),
    OPERATION_NOT_PERMITTED(306,HttpStatus.BAD_REQUEST,"Opération non Autorisée"),
    FILE_STORAGE(307,HttpStatus.INSUFFICIENT_STORAGE,"Problème de stockage");
    private final int code;
    private final HttpStatus httpStatus;
    private final String description;

    BusinessErrorCodes(int code, HttpStatus status, String description){
        this.code= code;
        this.httpStatus = status;
        this.description = description;
    }

}
