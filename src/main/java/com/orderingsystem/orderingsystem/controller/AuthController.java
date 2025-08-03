package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.*;
import com.orderingsystem.orderingsystem.dto.response.LoginResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.dto.response.SignupResponse;
import com.orderingsystem.orderingsystem.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authManager;

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
}
