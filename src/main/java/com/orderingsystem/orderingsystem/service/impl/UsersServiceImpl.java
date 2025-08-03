package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.UsersRequest;
import com.orderingsystem.orderingsystem.dto.response.UsersResponse;
import com.orderingsystem.orderingsystem.entity.Bills;
import com.orderingsystem.orderingsystem.entity.Users;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
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

    /* ---------- CREATE ---------- */
    @Override
    public UsersResponse createUser(UsersRequest request) {
        request.setStatus((byte) 1);
        request.setPoint(0);

        validate(request, false);

        Users user = new Users();
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPoint(request.getPoint());
        user.setStatus(request.getStatus());

        return toResponse(usersRepository.save(user));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public UsersResponse updateUser(Integer id, UsersRequest request) {
        if (id == 1) {
            throw new RuntimeException("Cannot update this user (ID = 1)");
        }

        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        validate(request, true);

        user.setUsername(request.getUsername().trim());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setPoint(request.getPoint());
        user.setStatus(request.getStatus());

        return toResponse(usersRepository.save(user));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public UsersResponse softDeleteUser(Integer id) {
        if (id == 1) {
            throw new RuntimeException("Cannot soft delete this user (ID = 1)");
        }

        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));

        user.setStatus((byte)0);

        return toResponse(usersRepository.save(user));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteUser(Integer id) {
        if (id == 1) {
            throw new RuntimeException("Cannot delete this user (ID = 1)");
        }

        if (!usersRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        List<Bills> billsList = billsRepository.findByUser_id(id);
        for (Bills b : billsList) {
            b.setUser(usersRepository.getReferenceById(1));
        }
        billsRepository.saveAll(billsList);

        usersRepository.deleteById(id);
    }

    /* ---------- READ ---------- */
    @Override
    public UsersResponse getUserById(Integer id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return toResponse(user);
    }

    @Override
    public List<UsersResponse> getAllUsers() {
        return usersRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /* ---------- PRIVATE HELPERS ---------- */
    private void validate(UsersRequest request, boolean isUpdate) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new BusinessRuleException("Username must not be empty");
        }
        if (!isUpdate && usersRepository.existsByUsername(request.getUsername().trim())) {
            throw new BusinessRuleException("Username already exists");
        }
        if (request.getPoint() == null || request.getPoint() < 0) {
            throw new BusinessRuleException("Point must be a non‑negative number");
        }
        if (request.getStatus() != 0 && request.getStatus() != 1) {
            throw new BusinessRuleException("Status must be 0 (inactive) or 1 (active)");
        }
    }

    private UsersResponse toResponse(Users user) {
        return UsersResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .point(user.getPoint())
                .status(user.getStatus())
                .build();
    }
}
