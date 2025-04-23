package com.microservices.controller;

import com.microservices.dto.LoginRequest;
import com.microservices.dto.LoginResponse;
import com.microservices.dto.RegisterRequest;
import com.microservices.dto.RegisterResponse;
import com.microservices.entities.AuthUser;
import com.microservices.security.JwtService;
import com.microservices.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest request) {
        var user = authService.authenticate(request.email(), request.password());

        if (user.isEmpty()) {
            return ResponseEntity.status(401).body("Email ou senha inválidos");
        }

        String token = jwtService.generateToken(user.get().getUserId(), user.get().getRole());
        return ResponseEntity.ok(new LoginResponse(token, user.get().getName()));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthUser registeredUser = authService.register(registerRequest);
        RegisterResponse response = new RegisterResponse(
                registeredUser.getName(),
                registeredUser.getEmail()
        );
        return ResponseEntity.status(201).body(response);
    }
    
}

