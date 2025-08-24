package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.*;
import com.orderingsystem.orderingsystem.dto.response.LoginResponse;
import com.orderingsystem.orderingsystem.dto.response.SignupResponse;
import com.orderingsystem.orderingsystem.entity.Role;
import com.orderingsystem.orderingsystem.entity.Status;
import com.orderingsystem.orderingsystem.entity.Users;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.UsernameException;
import com.orderingsystem.orderingsystem.repository.UsersRepository;
import com.orderingsystem.orderingsystem.config.JwtTokenProvider;
import com.orderingsystem.orderingsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepo;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtProvider;

    public SignupResponse signup(SignUpRequest req) {
        if (usersRepo.existsByUsername(req.getUsername()))
            throw new UsernameException("Username already taken");

        Users user = new Users();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setPoint(0);
        user.setStatus(Status.ACTIVE);
        user.setRole(req.getRole() != null ? req.getRole() : Role.USER);

        usersRepo.save(user);

        return new SignupResponse("Signup Successful");
    }

    public LoginResponse login(LoginRequest req, AuthenticationManager authManager) {
        Users user = usersRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid"));

        if (user.getStatus() == Status.INACTIVE) {
            throw new BusinessRuleException("Account is inactive");
        }

        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        String role = user.getRole().name();
        String token = jwtProvider.generateAccessToken(user.getUsername(), role);
        String refreshToken = jwtProvider.generateRefreshToken(user.getUsername());

        return new LoginResponse(token, refreshToken, user.getUsername(), role);
    }
}
