package com.soumaya.orders_management_app.backend.AppControllers.auth;

import com.soumaya.orders_management_app.backend.ExceptionHandling.OperationNotPermittedException;
import com.soumaya.orders_management_app.backend.Models.User.Role;
import com.soumaya.orders_management_app.backend.Models.User.User;
import com.soumaya.orders_management_app.backend.Models.User.UserRepository;
import com.soumaya.orders_management_app.backend.Security.JwtService;
import com.soumaya.orders_management_app.backend.common.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public StandardResponse registerUser(RegisterRequest request){

        if (userRepository.existsByUsername(request.getUsername())){
            throw new OperationNotPermittedException(
                    "un utilisateur ayant le même nom d'utilisateur existe déjà" +
                    " ou pas supprimé définitivement, veuillez choisir un autre"
            );
        }


        Set<Role> roles = request.getRoles().stream()
                .map(String::toUpperCase)
                .map(r -> {
                    try{
                        return Role.valueOf(r);
                    }catch (IllegalArgumentException e){
                        throw new IllegalArgumentException("le role: "+ r + "n'est pas valide");
                    }
                })
                .collect(Collectors.toSet());


        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .tel(request.getTel())
                .username(request.getUsername())
                .roles(roles)
                .deleted(false)
                .enabled(true)
                .accountLocked(false)
                .build();

        return StandardResponse.builder()
                .id(userRepository.save(user).getId())
                .message("utilisateur enregistrer!")
                .build();

    }

    public LoginResponse login(LoginRequest request){

        // authenticate the user
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        //build the token
        User user = (User) authentication.getPrincipal();
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("fullName", user.getFullName());

        String token = jwtService.generateToken(user, extraClaims);
        return LoginResponse.builder()
                .id(user.getId())
                .token(token)
                .Roles(user.getRoles())
                .build();
    }
}
