package com.cryptotracker.user_service.dto;

import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
  private UUID id;
  private String email;
  private String name;
  private Set<String> roles;
}