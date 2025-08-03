package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.UsersRequest;
import com.orderingsystem.orderingsystem.dto.response.UsersResponse;

import java.util.List;

public interface UsersService {
    UsersResponse createUser(UsersRequest request);
    UsersResponse updateUser(Integer id, UsersRequest request);
    UsersResponse softDeleteUser(Integer id);
    void deleteUser(Integer id);
    UsersResponse getUserById(Integer id);
    List<UsersResponse> getAllUsers();
}
