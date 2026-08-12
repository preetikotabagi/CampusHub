package com.preeti.campushub.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.preeti.campushub.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestEmailController {

    private final EmailService emailService;

    @GetMapping("/test-email")
    public String sendTestEmail() {

        emailService.sendEmail(
                "prkotabagi123@gmail.com",
                "CampusHub Test Email",
                "Congratulations! CampusHub can now send emails successfully."
        );

        return "Email Sent Successfully!";
    }
}