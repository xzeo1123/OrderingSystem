package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.UsersRequest;
import com.orderingsystem.orderingsystem.dto.response.UsersResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin
public class UsersController {

    private final UsersService usersService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UsersRequest usersRequest) {
        UsersResponse created = usersService.createUser(usersRequest);
        return ResponseHelper.created(created, "User created successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId, @RequestBody @Valid UsersRequest usersRequest) {
        UsersResponse updated = usersService.updateUser(userId, usersRequest);
        return ResponseHelper.ok(updated, "User updated successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @PutMapping("/delete/{userId}")
    public ResponseEntity<?> softDeleteUser(@PathVariable Integer userId) {
        UsersResponse updated = usersService.softDeleteUser(userId);
        return ResponseHelper.ok(updated, "User soft deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        usersService.deleteUser(userId);
        return ResponseHelper.deleted("User deleted successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Integer userId) {
        UsersResponse user = usersService.getUserById(userId);
        return ResponseHelper.ok(user, "Get user by ID successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UsersResponse> users = usersService.getAllUsers();
        return ResponseHelper.ok(users, "Get list of users successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<?> searchUsersByUsername(@RequestParam String username) {
        List<UsersResponse> users = usersService.searchUsersByUsername(username);
        return ResponseHelper.ok(users, "Search users successfully");
    }
}
