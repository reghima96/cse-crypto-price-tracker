package com.cryptotracker.user_service.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cryptotracker.user_service.service.UserService;

@Service
public class AppUserDetailsService implements UserDetailsService, UserDetailsPasswordService {

	private final UserService userService;

	public AppUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails updatePassword(UserDetails user, String newPassword) {
		return this.userService
				.findByEmail(user.getUsername())
				.map(u -> {
					u.setPassword(newPassword);

					final var updatedUser = this.userService.save(u);
					return new AuthenticatedUser(updatedUser);
				})
				.orElseThrow(() -> new UsernameNotFoundException("The user with user name " + user.getUsername() + " could not be found"));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.userService
				.findByEmail(username)
				.map(AuthenticatedUser::new)
				.orElseThrow(() -> new UsernameNotFoundException("The user with user name " + username + " could not be found"));
	}

}
