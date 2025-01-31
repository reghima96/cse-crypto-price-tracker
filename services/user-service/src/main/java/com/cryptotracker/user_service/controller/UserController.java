package com.cryptotracker.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cryptotracker.user_service.dto.UserProfileResponse;
import com.cryptotracker.user_service.repository.UserEntity;
import com.cryptotracker.user_service.security.AuthenticatedUser;
import com.cryptotracker.user_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<UserProfileResponse> getCurrentUser(@AuthenticationPrincipal AuthenticatedUser user) {
    log.debug("Getting profile for user: {}", user != null ? user.getEmail() : "null");
    
    if (user == null) {
      log.warn("No authenticated user found");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return userService.findById(user.getId())
        .map(this::toProfileResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  private UserProfileResponse toProfileResponse(UserEntity user) {
    return new UserProfileResponse(
        user.getId(),
        user.getEmail(),
        user.getName(),
        user.getRoles());
  }
}