package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.config.JwtTokenProvider;
import com.orderingsystem.orderingsystem.dto.request.*;
import com.orderingsystem.orderingsystem.dto.response.LoginResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.dto.response.SignupResponse;
import com.orderingsystem.orderingsystem.entity.Users;
import com.orderingsystem.orderingsystem.repository.UsersRepository;
import com.orderingsystem.orderingsystem.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtProvider;
    private final UsersRepository usersRepo;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequest req) {
        SignupResponse res = authService.signup(req);
        return ResponseHelper.ok(null, res.getMessage());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest req) {
        LoginResponse res = authService.login(req, authManager);
        return ResponseHelper.ok(res, "Login successful");
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest req) {
        String rt = req.getRefreshToken();
        if (!jwtProvider.validateRefreshToken(rt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = jwtProvider.getUsernameFromRefresh(rt);
        Users user = usersRepo.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        String role = user.getRole().name();
        String newAccess = jwtProvider.generateAccessToken(username, role);

        return ResponseEntity.ok(new LoginResponse(newAccess, rt, username, role));
    }
}
