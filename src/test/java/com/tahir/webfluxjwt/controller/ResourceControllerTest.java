package com.tahir.webfluxjwt.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;


@AutoConfigureWebTestClient
@SpringBootTest
@ExtendWith(SpringExtension.class)
class ResourceControllerTest {

    private static final String VALID_JWT = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjo5MjIzMzcyMDM2ODU0Nzc1LCJpYXQiOjAsImVtYWlsIjoidGVzdEBlbWFpbC5jb20iLCJhdXRob3JpdGllcyI6WyJSZWFkIiwiV3JpdGUiLCJST0xFX1N1cGVyIiwiRXh0cmEiXX0.ZVw-pQLx8krJLuMwvueOOep31nCGUft0jZnlB8i46C57-b8P6Gbl1OcECxRgYnmRk8kdHzGWYZoPyUDMmupjcA";

    @Autowired
    WebTestClient webTestClient;

    private static final WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());

    @BeforeAll
    static void setUp() {
        wireMockServer.start();
        System.setProperty("jwt.jwks.url", "http://localhost:" + wireMockServer.port());
    }

    @AfterAll
    static void tearDown() {
        if(wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    @Test
    void getPublicResource() {
        webTestClient.get()
                .uri("/public/resource")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().jsonPath("$.message").isEqualTo("Hello World");
    }

    @Test
    void getPrivateResource_withoutAuthentication_shouldBeUnauthorized() {
        webTestClient.get()
                .uri("/private/resource")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED)
                .expectBody().isEmpty();
    }

    @Test
    void getPrivateResource_withJwtAuthentication_shouldReturnResult() {
        webTestClient.get()
                .uri("/private/resource")
                .header(HttpHeaders.AUTHORIZATION, VALID_JWT)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().jsonPath("$.message").isEqualTo("Hello user 1");
    }

    @Test
    void getPrivateResource_withJwtAuthentication_withoutValidRole_shouldBeUnauthorized() {
        webTestClient.get()
                .uri("/private/uberRoleResource")
                .header(HttpHeaders.AUTHORIZATION, VALID_JWT)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN)
                .expectBody().isEmpty();
    }

    @Test
    void getPrivateResource_withJwtAuthentication_withValidRole_shouldReturnResult() {
        webTestClient
                .mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_Uber")))
                .get()
                .uri("/private/uberRoleResource")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().jsonPath("$.message").isEqualTo("Hello Uber user user");
    }
}
