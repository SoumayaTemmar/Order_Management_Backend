package com.soumaya.orders_management_app.backend.Models.User;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public enum Role {
    ADMIN("admin","ROLE_ADMIN"),
    VENDEUR("vendeur","ROLE_VENDEUR"),
    RESPONSABLE_CMD("responsable commandes","ROLE_RESPONSABLE_CMD");

    private final String description;
    private final String authority;

    Role(String description, String authority){
        this.description = description;
        this.authority = authority;
    }

    public SimpleGrantedAuthority getAsAuthority(){
        return new SimpleGrantedAuthority(authority);
    }

}
