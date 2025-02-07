package com.cryptotracker.api_gateway_service.filter;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final SecretKey secretKey;
    private final List<String> excludedPaths = Arrays.asList(
            "/auth/login",
            "/auth/register");

    public JwtAuthFilter(@Value("${jwt.secret}") String secret) {
        super(Config.class);
        log.info("JwtAuthFilter constructor called");
        log.info("Secret: {}", secret);
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        log.info("SecretKey: {}", this.secretKey);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().value();
            String method = request.getMethod().toString();

            log.debug("Incoming request: {} {}", method, path);

            // Exclude login and register paths from authentication
            if (excludedPaths.stream().anyMatch(path::startsWith)) {
                log.debug("Skipping JWT authentication for path: {}", path);
                return chain.filter(exchange);
            }
            log.debug("Extracting token from headers");
            log.debug("Request headers: {}", request.getHeaders());
            // Extract token from headers
            String token = extractToken(request);
            if (token == null) {
                log.warn("Missing authentication token for path: {}", path);
                return onError(exchange.getResponse(), "Missing authentication token", HttpStatus.UNAUTHORIZED);
            }

            Claims claims;
            try {
                claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            } catch (Exception e) {
                log.error("JWT Validation Failed: {}", e.getMessage());
                return onError(exchange.getResponse(), "Invalid token", HttpStatus.UNAUTHORIZED);
            }

            // Extract user details
            String email = claims.getSubject();
            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);
            String rolesString = roles != null ? String.join(",", roles) : "";

            log.debug("Authenticated user: {}, Roles: {}", email, rolesString);

            // Forward user details to microservices
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Email", email)
                    .header("X-User-Roles", rolesString)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    private String extractToken(ServerHttpRequest request) {
        // First try from cookie
        String token = null;
        HttpCookie tokenCookie = request.getCookies().getFirst("token");
        if (tokenCookie != null) {
            token = tokenCookie.getValue();
        }

        // If no cookie, try from Authorization header
        if (token == null) {
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        return token;
    }

    private Mono<Void> onError(ServerHttpResponse response, String message, HttpStatus status) {
        log.warn("Authorization error: {}, Status: {}", message, status);
        response.setStatusCode(status);
        return response.setComplete();
    }

    public static class Config {
        // Add configuration properties if needed
    }
}
