package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.UsersRequest;
import com.orderingsystem.orderingsystem.dto.response.UsersResponse;

import java.util.List;

public interface UsersService {
    UsersResponse createUser(UsersRequest usersRequest);
    UsersResponse updateUser(Integer userId, UsersRequest usersRequest);
    UsersResponse softDeleteUser(Integer userId);
    void deleteUser(Integer userId);
    UsersResponse getUserById(Integer userId);
    List<UsersResponse> getAllUsers();
    List<UsersResponse> searchUsersByUsername(String username);
}
