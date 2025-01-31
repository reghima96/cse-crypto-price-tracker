package com.cryptotracker.api_gateway_service.security;

import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

  private final SecretKey key;

  public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret) {
    byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public Claims parseToken(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      log.error("JWT validation failed: {}", e.getMessage());
      return false;
    }
  }

  public String getEmailFromToken(String token) {
    return parseToken(token).get("email", String.class);
  }

  public List<String> getRolesFromToken(String token) {
    @SuppressWarnings("unchecked")
    List<String> roles = parseToken(token).get("roles", List.class);
    return roles;
  }
}