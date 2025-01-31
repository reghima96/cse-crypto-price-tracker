package com.cryptotracker.user_service.model;

public enum RoleType {
  USER, ADMIN;

  public static boolean isValid(String role) {
    try {
      RoleType.valueOf(role.toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}