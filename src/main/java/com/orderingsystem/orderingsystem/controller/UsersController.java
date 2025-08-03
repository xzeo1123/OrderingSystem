package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.UsersRequest;
import com.orderingsystem.orderingsystem.dto.response.BillsResponse;
import com.orderingsystem.orderingsystem.dto.response.UsersResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin
public class UsersController {

    private final UsersService usersService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UsersRequest request) {
        UsersResponse created = usersService.createUser(request);
        return ResponseHelper.created(created, "User created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid UsersRequest request) {
        UsersResponse updated = usersService.updateUser(id, request);
        return ResponseHelper.ok(updated, "User updated successfully");
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteUser(@PathVariable Integer id) {
        UsersResponse updated = usersService.softDeleteUser(id);
        return ResponseHelper.ok(updated, "User soft deleted successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        usersService.deleteUser(id);
        return ResponseHelper.deleted("User deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        UsersResponse user = usersService.getUserById(id);
        return ResponseHelper.ok(user, "Get user by ID successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UsersResponse> users = usersService.getAllUsers();
        return ResponseHelper.ok(users, "Get list of users successfully");
    }
}
