package com.cryptotracker.user_service.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.cryptotracker.user_service.repository.UserEntity;

public class AuthenticatedUser extends UserEntity implements UserDetails {

	public AuthenticatedUser(UserEntity userEntity) {
		super();

		this.setId(userEntity.getId());
		this.setName(userEntity.getName());
		this.setEmail(userEntity.getEmail());
		this.setPassword(userEntity.getPassword());
		this.setRoles(userEntity.getRoles());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", getRoles()));
	}

	@Override
	public String getUsername() {
		// use the email as user name
		return this.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "AuthenticatedUser [username=" + getUsername() + ", id=" + getId() + "]";
	}
}
