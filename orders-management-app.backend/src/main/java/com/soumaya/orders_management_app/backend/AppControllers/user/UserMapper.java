package com.soumaya.orders_management_app.backend.AppControllers.user;

import com.soumaya.orders_management_app.backend.ExceptionHandling.DuplicateUsernameException;
import com.soumaya.orders_management_app.backend.Models.User.Role;
import com.soumaya.orders_management_app.backend.Models.User.User;
import com.soumaya.orders_management_app.backend.Models.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final UserRepository userRepository;

    public UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .roles(user.getRoles())
                .tel(user.getTel())
                .build();
    }
    public User updateUser(UpdateRequest request, User user){
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        //check if the username is already taken
        if (!user.getUsername().equals(request.getUsername()) &&
                userRepository.existsByUsername(request.getUsername())
        ){
            throw new DuplicateUsernameException(
                    "un utilisateur avec le même nom d'utilisateur existe déjà" +
                    " ou pas supprimé définitivement, veuillez choisir un autre"

            );

        }
        user.setUsername(request.getUsername());
        user.setTel(request.getTel());

        Set<Role> roles = request.getRoles().stream()
                .map(String::toUpperCase)
                .map(r -> {
                    try{
                        return Role.valueOf(r);
                    }catch (IllegalArgumentException e){
                        throw new IllegalArgumentException(
                                "role: "+ r + " n'est pas valid !"
                        );
                    }
                })
                .collect(Collectors.toSet());

        user.setRoles(roles);
        return user;
    }
}
