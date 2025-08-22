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
    public ResponseEntity<?> createUser(@RequestBody @Valid UsersRequest request) {
        UsersResponse created = usersService.createUser(request);
        return ResponseHelper.created(created, "User created successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid UsersRequest request) {
        UsersResponse updated = usersService.updateUser(id, request);
        return ResponseHelper.ok(updated, "User updated successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteUser(@PathVariable Integer id) {
        UsersResponse updated = usersService.softDeleteUser(id);
        return ResponseHelper.ok(updated, "User soft deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        usersService.deleteUser(id);
        return ResponseHelper.deleted("User deleted successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        UsersResponse user = usersService.getUserById(id);
        return ResponseHelper.ok(user, "Get user by ID successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UsersResponse> users = usersService.getAllUsers();
        return ResponseHelper.ok(users, "Get list of users successfully");
    }
}
