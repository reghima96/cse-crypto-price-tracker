package com.cryptotracker.user_service.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cryptotracker.user_service.dto.LoginRequest;
import com.cryptotracker.user_service.dto.LoginResponse;
import com.cryptotracker.user_service.dto.RegistrationRequest;
import com.cryptotracker.user_service.repository.UserEntity;
import com.cryptotracker.user_service.security.AuthenticatedUser;
import com.cryptotracker.user_service.security.JwtTokenProvider;
import com.cryptotracker.user_service.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/auth/login")
    public String loginPage(@RequestParam(required = false) String message, Model model) {
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "login";
    }

    @GetMapping("/auth/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("Login attempt for email: {}", loginRequest.getEmail());
        
        return userService.findByEmail(loginRequest.getEmail())
            .map(user -> {
                log.debug("User found: {}", user.getEmail());
                if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    log.debug("Password matches for user: {}", user.getEmail());
                    String token = jwtTokenProvider.generateToken(new AuthenticatedUser(user));
                    return ResponseEntity.ok(new LoginResponse(token, "/api/prices/dashboard"));
                }
                log.debug("Password does not match for user: {}", user.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).<LoginResponse>build();
            })
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).<LoginResponse>build());
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        try {
            UserEntity user = new UserEntity();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setName(request.getName());
            user.setRoles(Set.of("USER"));

            userService.save(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

