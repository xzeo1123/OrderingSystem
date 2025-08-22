package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.UsersRequest;
import com.orderingsystem.orderingsystem.dto.response.UsersResponse;
import com.orderingsystem.orderingsystem.entity.Role;
import com.orderingsystem.orderingsystem.entity.Status;
import com.orderingsystem.orderingsystem.entity.Users;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.mapping.UsersMapper;
import com.orderingsystem.orderingsystem.repository.BillsRepository;
import com.orderingsystem.orderingsystem.repository.UsersRepository;
import com.orderingsystem.orderingsystem.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final BillsRepository billsRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersMapper usersMapper;

    /* ---------- CREATE ---------- */
    @Override
    public UsersResponse createUser(UsersRequest usersRequest) {
        usersRequest.setPoint(0);
        usersRequest.setStatus(Status.ACTIVE);
        usersRequest.setRole(usersRequest.getRole() != null ? usersRequest.getRole() : Role.USER);

        Users user = usersMapper.toEntity(usersRequest);
        user.setUsername(usersRequest.getUsername().trim());
        user.setPassword(passwordEncoder.encode(usersRequest.getPassword()));

        return usersMapper.toResponse(usersRepository.save(user));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public UsersResponse updateUser(Integer userId, UsersRequest usersRequest) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        validate(usersRequest);

        Users updatedUser = usersMapper.toEntity(usersRequest);
        updatedUser.setId(user.getId());
        updatedUser.setUsername(usersRequest.getUsername().trim());
        if (usersRequest.getPassword() != null && !usersRequest.getPassword().isBlank()) {
            updatedUser.setPassword(passwordEncoder.encode(usersRequest.getPassword()));
        }

        return usersMapper.toResponse(usersRepository.save(updatedUser));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public UsersResponse softDeleteUser(Integer userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));

        user.setStatus(Status.INACTIVE);

        return usersMapper.toResponse(usersRepository.save(user));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteUser(Integer userId) {
        if (!usersRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        }

        usersRepository.deleteById(userId);
    }

    /* ---------- READ ---------- */
    @Override
    public UsersResponse getUserById(Integer userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        return usersMapper.toResponse(user);
    }

    @Override
    public List<UsersResponse> getAllUsers() {
        return usersRepository.findAll()
                .stream()
                .map(usersMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UsersResponse> searchUsersByUsername(String username) {
        return usersRepository.findByUsernameContainingIgnoreCase(username)
                .stream()
                .map(usersMapper::toResponse)
                .toList();
    }


    /* ---------- PRIVATE HELPERS ---------- */
    private void validate(UsersRequest usersRequest) {
        if (usersRepository.existsByUsername(usersRequest.getUsername().trim())) {
            throw new BusinessRuleException("Username already exists");
        }
    }
}
