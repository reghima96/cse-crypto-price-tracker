package com.cryptotracker.user_service.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cryptotracker.user_service.exception.InvalidRoleException;
import com.cryptotracker.user_service.exception.UserNotFoundException;
import com.cryptotracker.user_service.model.RoleType;
import com.cryptotracker.user_service.repository.UserEntity;
import com.cryptotracker.user_service.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity registerUser(String name, String email, String rawPassword, Set<String> roles) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        UserEntity user = new UserEntity();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public Optional<UserEntity> findById(UUID id) {
        return this.userRepository.findById(id);
    }

    public Optional<UserEntity> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    public UserEntity save(UserEntity user) {
        log.debug("Saving user: {}", user);
        try {
            UserEntity savedUser = userRepository.save(user);
            log.debug("Successfully saved user: {}", savedUser.getEmail());
            return savedUser;
        } catch (Exception e) {
            log.error("Error saving user: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void addRole(String email, String role) {
        if (!RoleType.isValid(role)) {
            throw new InvalidRoleException(role);
        }

        UserEntity user = findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        Set<String> roles = new HashSet<>(user.getRoles());
        if (roles.contains(role)) {
            throw new IllegalStateException("User already has role: " + role);
        }

        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);
        log.debug("Added role {} to user {}", role, email);
    }

}
