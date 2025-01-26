package com.cryptotracker.user_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Renders login.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // Renders register.html
    }
}
