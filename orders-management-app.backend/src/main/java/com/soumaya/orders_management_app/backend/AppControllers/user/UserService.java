package com.soumaya.orders_management_app.backend.AppControllers.user;

import com.soumaya.orders_management_app.backend.Models.User.User;
import com.soumaya.orders_management_app.backend.Models.User.UserRepository;
import com.soumaya.orders_management_app.backend.common.PageResponse;
import com.soumaya.orders_management_app.backend.common.StandardResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // get list of users using pagination (non deleted users)
    public PageResponse<UserResponse> getUsers(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate"));
        Page<User> users = userRepository.findAllByDeletedFalse(pageable);

        List<UserResponse> userResponses = users.stream()
                .map(userMapper::toUserResponse)
                .toList();

        return new PageResponse<>(
                userResponses,
                users.getNumber(),
                users.getSize(),
                users.getTotalPages(),
                users.getTotalElements()
        );
    }
    // get list of users using pagination (deleted users)
    public PageResponse<UserResponse> getDeletedUsers(int page, int size){

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate"));
        Page<User> users = userRepository.findAllByDeletedTrue(pageable);

        List<UserResponse> userResponses = users.stream()
                .map(userMapper::toUserResponse)
                .toList();

        return new PageResponse<>(
                userResponses,
                users.getNumber(),
                users.getSize(),
                users.getTotalPages(),
                users.getTotalElements()
        );
    }

    // get user by his id
    public UserResponse getUserById(int id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "utilisateur avec id: "+ id+ "n'exist pas"
                ));

        return userMapper.toUserResponse(user);
    }

    // soft delete user
    @Transactional
    public StandardResponse softDeleteUser(int id){

        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "utilisateur avec id: "+ id+ "n'exist pas"
                ));

        if (user.isDeleted()){
            throw new IllegalStateException(
                    "utilisateur est déjà supprimé, veuillez vérifier la corbeille"
            );
        }
        user.setDeleted(true);
        userRepository.save(user);

        return StandardResponse.builder()
                .id(id)
                .message("utilisateur supprimé !").build();
    }

    // restore deleted users
    @Transactional
    public StandardResponse restoreUser(int id){

        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "utilisateur avec id: "+ id+ "n'exist pas"
                ));

        if (!user.isDeleted()){
            throw new IllegalStateException(
                    "utilisateur est déjà restoré !"
            );
        }
        user.setDeleted(false);
        userRepository.save(user);

        return StandardResponse.builder()
                .id(id)
                .message("utilisateur restoré !").build();
    }

    // delete user permanently
    @Transactional
    public StandardResponse deleteUser(int id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "utilisateur avec id: "+ id+ "n'exist pas"
                ));

        userRepository.delete(user);
        return StandardResponse.builder()
                .id(id)
                .message("utilisateur supprimé définitivement !")
                .build();
    }
    // update user
    @Transactional
    public StandardResponse updateUser(int id, UpdateRequest request){

        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "utilisateur avec id: "+ id+ "n'exist pas"
                ));

        userRepository.save(userMapper.updateUser(request,user));
        return StandardResponse.builder()
                .id(id)
                .message("utilisateur modifié !")
                .build();
    }

    @Transactional
    public StandardResponse updatePwd(
            Authentication connectedUser,
            UpdatePwdRequest request
    ){
        User user = (User) connectedUser.getPrincipal();
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new IllegalArgumentException(
                    "ancien mot de passe n'est pas correct !"
            );
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return StandardResponse.builder()
                .id(user.getId())
                .message("mot de passe modifié !")
                .build();
    }

}
