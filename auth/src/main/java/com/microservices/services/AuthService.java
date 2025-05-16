package com.microservices.services;

import com.microservices.dto.RegisterRequest;
import com.microservices.entities.AuthUser;
import com.microservices.repositories.AuthUserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Optional<AuthUser> authenticate(String email, String password) {
        Optional<AuthUser> user = authUserRepository.findByEmail(email);

        if (user.isPresent()) {
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return user;
            }
        }

        return Optional.empty();
    }

    
    public AuthUser register(RegisterRequest registerRequest){
        if (authUserRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new RuntimeException("Essse email já foi cadastrado");
        }
            
        String userUUID = UUID.randomUUID().toString();

        AuthUser authUser = new AuthUser();
        authUser.setUserId(userUUID);
        authUser.setName(registerRequest.name());
        authUser.setEmail(registerRequest.email());
        authUser.setPassword(passwordEncoder.encode(registerRequest.password()));
        authUser.setRole(registerRequest.role());

        return authUserRepository.save(authUser);
    }

    public List<AuthUser> getAllUsers() {
        return authUserRepository.findAll();
    }

}
