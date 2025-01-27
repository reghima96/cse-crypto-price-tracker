package com.cryptotracker.user_service.controller;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cryptotracker.user_service.dto.RegistrationRequest;
import com.cryptotracker.user_service.service.UserService;

import jakarta.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("registrationRequest")) {
            model.addAttribute("registrationRequest", new RegistrationRequest());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationRequest") RegistrationRequest registrationRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationRequest", bindingResult);
            redirectAttributes.addFlashAttribute("registrationRequest", registrationRequest);
            return "redirect:/register";
        }

        try {
            userService.registerUser(registrationRequest.getName(), registrationRequest.getEmail(), registrationRequest.getPassword(), Collections.singleton("USER"));
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "error.user", e.getMessage());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationRequest", bindingResult);
            redirectAttributes.addFlashAttribute("registrationRequest", registrationRequest);
            return "redirect:/register";
        }
    }
}