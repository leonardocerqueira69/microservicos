package com.microservices.services;

import com.microservices.entities.AuthUser;
import com.microservices.repositories.AuthUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository authUserRepository;

    public Optional<AuthUser> authenticate(String email, String password) {
        Optional<AuthUser> user = authUserRepository.findByEmail(email);

        // DEBUG: ajuda a verificar o que está acontecendo
        System.out.println("Email recebido: " + email);
        System.out.println("Senha recebida: " + password);
        System.out.println("Usuário encontrado? " + user.isPresent());

        if (user.isPresent()) {
            System.out.println("Senha no banco: " + user.get().getPassword());
            if (user.get().getPassword().equals(password)) {
                return user;
            }
        }

        return Optional.empty();
    }
}
