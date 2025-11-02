package com.soumaya.orders_management_app.backend.AppControllers.user;

import com.soumaya.orders_management_app.backend.Models.User.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
public class UserResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private Set<Role> roles;
    private String tel;

}
