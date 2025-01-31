package com.cryptotracker.user_service.exception;

public class InvalidRoleException extends RuntimeException {
  public InvalidRoleException(String role) {
    super("Invalid role: " + role);
  }
}