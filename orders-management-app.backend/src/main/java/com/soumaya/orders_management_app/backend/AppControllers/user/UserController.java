package com.soumaya.orders_management_app.backend.AppControllers.user;

import com.soumaya.orders_management_app.backend.common.PageResponse;
import com.soumaya.orders_management_app.backend.common.StandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //get all users(non soft deleted) using pagination
    @GetMapping("/all")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(userService.getUsers(page, size));
    }
    // get all users(soft deleted) using pagination
    @GetMapping("/trash")
    public ResponseEntity<PageResponse<UserResponse>> getAllDeletedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(userService.getDeletedUsers(page, size));
    }
    // get a user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable int id
    ){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // soft delete a user
    @PatchMapping("/delete/soft/{id}")
    public ResponseEntity<StandardResponse> softDeleteUser(
            @PathVariable int id
    ){
        return ResponseEntity.ok(userService.softDeleteUser(id));
    }
    // restore deleted user
    @PatchMapping("/trash/restore/{id}")
    public ResponseEntity<StandardResponse> restoreUser(
            @PathVariable int id
    ){
        return ResponseEntity.ok(userService.restoreUser(id));
    }

    // delete user permanently
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StandardResponse> deleteUser(
            @PathVariable int id
    ){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    // update a user

    @PutMapping("/update/{id}")
    public ResponseEntity<StandardResponse> updateUser(
            @PathVariable int id,
            @RequestBody @Valid UpdateRequest request
    ){
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    //update password by the user himself
    @PatchMapping("password/update")
    public ResponseEntity<StandardResponse> updatePwd(
            @RequestBody @Valid UpdatePwdRequest request,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(userService.updatePwd(connectedUser, request));
    }

}
