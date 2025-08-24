package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.LoginRequest;
import com.orderingsystem.orderingsystem.dto.request.SignUpRequest;
import com.orderingsystem.orderingsystem.dto.response.LoginResponse;
import com.orderingsystem.orderingsystem.dto.response.SignupResponse;
import org.springframework.security.authentication.AuthenticationManager;

public interface AuthService {
    SignupResponse signup(SignUpRequest signUpRequest);
    LoginResponse login(LoginRequest req, AuthenticationManager authManager);
}
