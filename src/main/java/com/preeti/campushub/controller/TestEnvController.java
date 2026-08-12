package com.preeti.campushub.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEnvController {

    @Value("${MAIL_USERNAME}")
    private String username;

    @GetMapping("/test-env")
    public String testEnv() {
        return username;
    }
}