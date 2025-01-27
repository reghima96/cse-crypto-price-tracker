package com.cryptotracker.user_service.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cryptotracker.user_service.security.AuthenticatedUser;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (authenticatedUser != null) {
            model.addAttribute("username", authenticatedUser.getName());
        }
        return "dashboard";
    }
}