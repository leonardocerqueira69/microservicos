package com.microservices.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auth_user")
@Builder
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role;

}
