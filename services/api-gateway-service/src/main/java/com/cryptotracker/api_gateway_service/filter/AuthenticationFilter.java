package com.cryptotracker.api_gateway_service.filter;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.cryptotracker.api_gateway_service.security.JwtTokenProvider;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        // Skip authentication for public endpoints
        if (isPublicEndpoint(path)) {
            log.debug("Skipping authentication for public path: {}", path);
            return chain.filter(exchange);
        }

        String token = extractToken(exchange.getRequest());
        log.debug("Processing request for path: {}", path);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            log.debug("Invalid or missing token for path: {}", path);
            return unauthorizedResponse(exchange);
        }

        String userEmail = jwtTokenProvider.getEmailFromToken(token);
        List<String> roles = jwtTokenProvider.getRolesFromToken(token);
        log.debug("Authenticated user: {} with roles: {}", userEmail, roles);

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Email", userEmail)
                .header("X-User-Roles", String.join(",", roles))
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/auth/") ||
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.equals("/api/auth/login") ||
               path.equals("/api/auth/register");
    }

    private String extractToken(ServerHttpRequest request) {
        return Optional.ofNullable(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .orElse(null);
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
