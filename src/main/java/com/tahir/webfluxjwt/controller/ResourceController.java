package com.tahir.webfluxjwt.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

import static reactor.core.publisher.Mono.just;

@RestController
public class ResourceController {

    @GetMapping("/public/resource")
    public Mono<Map<String, Object>> hello() {
        return just(Map.of("message", "Hello World"));
    }

    @GetMapping("/private/resource")
    public Mono<Map<String, Object>> helloPrivate(@AuthenticationPrincipal Principal principal) {
        return just(Map.of("message", "Hello user " + principal.getName()));
    }

    @PreAuthorize("hasRole('Uber')")
    @GetMapping("/private/uberRoleResource")
    public Mono<Map<String, Object>> helloUber(@AuthenticationPrincipal Principal principal) {
        return just(Map.of("message", "Hello Uber user " + principal.getName()));
    }
}
