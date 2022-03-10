package com.tahir.webfluxjwt.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/resource")
    public Map<String, Object> hello() {
        return Map.of("message", "Hello World");
    }
}
