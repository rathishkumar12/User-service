package com.contactmanagementsystem.authentications;

import java.security.Principal;

import com.contactmanagementsystem.models.User;

public class UserPrincipal implements Principal {
	
	private User user;
	
	public UserPrincipal(User user) {
		super();
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String getName() {
		return user.getEmail();
	}

}