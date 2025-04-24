package com.microservices.auth;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AuthApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17.4")
            .withDatabaseName("authdb")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @LocalServerPort
    private int port;

    @BeforeEach 
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        String registerBody = """
                {
                    "name": "Leo",
                    "email": "leo@leo.com",
                    "password": "2810",
                    "role": "USER"
                }
                """;
        given()
            .contentType("application/json")
            .body(registerBody)
        .when()
            .post("/auth/register")
        .then()
            .statusCode(201)
            .body("name", equalTo("Leo"))
            .body("email", equalTo("leo@leo.com"));    
        
    }

    @Test
    void shouldLoginUserSuccessfully() {
        String registerBody = """
                {
                    "name": "Leo",
                    "email": "leo@leo.com",
                    "password": "2810",
                    "role": "USER"
                }
                """;
        given()
            .contentType("application/json")
            .body(registerBody)
        .when()
            .post("/auth/register")
        .then()
            .statusCode(201);

    
        String loginBody = """
                {
                    "email": "leo@leo.com",
                    "password": "2810"
                }   
                """;
        given()
            .contentType("application/json")
            .body(loginBody)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("email", equalTo("leo@leo.com"));
            
    }

}
