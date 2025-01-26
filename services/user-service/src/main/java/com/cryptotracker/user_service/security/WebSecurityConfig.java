package com.cryptotracker.user_service.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	@Primary
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
						.requestMatchers("/swagger-ui/*")
						.permitAll()
						.requestMatchers("/register", "/css/**", "/js/**")
						.permitAll() // Allow registration and static resources
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/actuator/**")
						.permitAll()
						.anyRequest()
						.authenticated())
				.httpBasic(withDefaults())
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/dashboard", true)
						.permitAll())
				.logout(logout -> logout
						.permitAll());

		return http.build();
	}
}
