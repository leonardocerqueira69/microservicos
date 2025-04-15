package com.microservices.auth;

import org.springframework.boot.SpringApplication;

import com.microservices.AuthApplication;

public class TestAuthApplication {

	public static void main(String[] args) {
		SpringApplication.from(AuthApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
