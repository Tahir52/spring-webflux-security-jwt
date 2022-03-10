package com.tahir.webfluxjwt.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;


@AutoConfigureWebTestClient
@SpringBootTest
@ExtendWith(SpringExtension.class)
class PublicControllerTest {

    @Autowired
    WebTestClient webTestClient;

    private static final WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());

    @BeforeTestClass
    static void setUp() {
        System.setProperty("jwt.jwks.url", "http://localhost:" + wireMockServer.port());

        wireMockServer.start();
    }

    @AfterTestClass
    static void tearDown() {
        if(wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    @Test
    void getResource() {

    }
}
