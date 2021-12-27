package com.revature.models;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.Arrays.stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserDetails is container for core user information. According to docs,
 * its implementations are not used directly by Spring Security for security
 * purposes. They simply store user information which is later encapsulated into
 * Authentication objects. This allows non-security related user information
 * (such as email addresses, telephone numbers etc) to be stored in a convenient
 * location.
 */
public class UserPrincipal implements UserDetails{

	// Below we tell Spring Security to handle our User object
	private User user;
	
	// Constructor injection;
	public UserPrincipal(User user) {
		this.user = user;
	}
	
	/**
	 * Below, be sure to return the password props of the User object that
	 * belongs to the UserPrincipal object.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
				
		// be sure to STATICALLY IMPORT java.util.Arrays.stream
		return stream(this.user.getAuthorities()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // we never implemented this is User model class, so change to true
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.user.isNotLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.user.isActive();
	}

}
