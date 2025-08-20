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
    public UsersResponse createUser(UsersRequest request) {
        request.setPoint(0);
        request.setStatus(Status.ACTIVE);
        request.setRole(request.getRole() != null ? request.getRole() : Role.USER);

        Users user = usersMapper.toEntity(request);
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return usersMapper.toResponse(usersRepository.save(user));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public UsersResponse updateUser(Integer id, UsersRequest request) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        validate(request);

        Users updatedUser = usersMapper.toEntity(request);
        updatedUser.setId(user.getId());
        updatedUser.setUsername(request.getUsername().trim());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            updatedUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return usersMapper.toResponse(usersRepository.save(updatedUser));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public UsersResponse softDeleteUser(Integer id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));

        user.setStatus(Status.INACTIVE);

        return usersMapper.toResponse(usersRepository.save(user));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteUser(Integer id) {
        if (!usersRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        usersRepository.deleteById(id);
    }

    /* ---------- READ ---------- */
    @Override
    public UsersResponse getUserById(Integer id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return usersMapper.toResponse(user);
    }

    @Override
    public List<UsersResponse> getAllUsers() {
        return usersRepository.findAll()
                .stream()
                .map(usersMapper::toResponse)
                .collect(Collectors.toList());
    }

    /* ---------- PRIVATE HELPERS ---------- */
    private void validate(UsersRequest request) {
        if (usersRepository.existsByUsername(request.getUsername().trim())) {
            throw new BusinessRuleException("Username already exists");
        }
    }
}
