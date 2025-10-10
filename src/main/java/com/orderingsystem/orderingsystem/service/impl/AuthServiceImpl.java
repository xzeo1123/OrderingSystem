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

//    public LoginResponse socialLogin(SocialLoginRequest req) {
//        String provider = req.getProvider().toLowerCase();
//        String providerId;
//
//        switch (provider) {
//            case "google":
//                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
//                        new NetHttpTransport(), new JacksonFactory())
//                        .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
//                        .build();
//                try {
//                    GoogleIdToken idToken = verifier.verify(req.getAccessToken());
//                    if (idToken == null) throw new BusinessRuleException("Invalid Google token");
//                    GoogleIdToken.Payload payload = idToken.getPayload();
//                    providerId = payload.getSubject();
//                } catch (Exception e) {
//                    throw new BusinessRuleException("Google verification failed: " + e.getMessage());
//                }
//                break;
//
//            case "github":
//                Map<String, Object> githubUser = fetchUserInfoFromGithub(req.getAccessToken());
//                providerId = String.valueOf(githubUser.get("id"));
//                break;
//
//            case "facebook":
//                Map<String, Object> fbUser = fetchUserInfoFromFacebook(req.getAccessToken());
//                providerId = String.valueOf(fbUser.get("id"));
//                break;
//
//            default:
//                throw new BusinessRuleException("Unsupported provider: " + provider);
//        }
//
//        String username = provider + "_" + providerId;
//
//        Users user = usersRepo.findByUsername(username)
//                .orElseGet(() -> {
//                    Users newUser = new Users();
//                    newUser.setUsername(username);
//                    newUser.setPassword(encoder.encode(UUID.randomUUID().toString()));
//                    newUser.setPoint(0);
//                    newUser.setStatus(Status.ACTIVE);
//                    newUser.setRole(Role.USER);
//                    return usersRepo.save(newUser);
//                });
//
//        String role = user.getRole().name();
//        String token = jwtProvider.generateAccessToken(user.getUsername(), role);
//        String refreshToken = jwtProvider.generateRefreshToken(user.getUsername());
//
//        return new LoginResponse(token, refreshToken, user.getUsername(), role);
//    }
//
//    private Map<String, Object> fetchUserInfoFromGithub(String accessToken) {
//        String url = "https://api.github.com/user";
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            return response.getBody();
//        }
//        throw new BusinessRuleException("Failed to fetch GitHub user info");
//    }
//
//    private Map<String, Object> fetchUserInfoFromFacebook(String accessToken) {
//        String url = "https://graph.facebook.com/me?fields=id,email,name&access_token=" + accessToken;
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            return response.getBody();
//        }
//        throw new BusinessRuleException("Failed to fetch Facebook user info");
//    }
}
