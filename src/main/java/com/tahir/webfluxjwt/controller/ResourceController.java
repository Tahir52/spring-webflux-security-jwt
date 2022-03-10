package com.tahir.webfluxjwt.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class ResourceController {

    @GetMapping("/public/resource")
    public Map<String, Object> hello() {
        return Map.of("message", "Hello World");
    }

    @GetMapping("/private/resource")
    public Map<String, Object> helloPrivate(@AuthenticationPrincipal Principal principal) {
        return Map.of("message", "Hello user " + principal.getName());
    }
}
