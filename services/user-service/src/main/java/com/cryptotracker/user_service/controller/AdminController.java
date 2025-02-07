package com.cryptotracker.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cryptotracker.user_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  private final UserService userService;

  @PostMapping("/users/{email}")
  public ResponseEntity<?> grantAdminRole(@PathVariable String email) {
    log.debug("Granting admin role to user: {}", email);
    userService.addRole(email, "ADMIN");
    return ResponseEntity.ok().build();
  }
}