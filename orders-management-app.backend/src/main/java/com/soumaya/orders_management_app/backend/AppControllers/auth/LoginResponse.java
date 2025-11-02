package com.soumaya.orders_management_app.backend.AppControllers.auth;

import com.soumaya.orders_management_app.backend.Models.User.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class LoginResponse {
    private int id;
    private String token;
    private Set<Role> Roles;
}
