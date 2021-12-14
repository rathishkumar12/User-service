package com.contactmanagementsystem.authentications;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import com.contactmanagementsystem.helpers.MD5;
import com.contactmanagementsystem.models.User;

public class UserLoginModule implements LoginModule {
	
	private CallbackHandler callbackHandler = null;
	private Subject subject = null;
	public CallbackHandler getCallbackHandler() {
		return callbackHandler;
	}

	public void setCallbackHandler(CallbackHandler callbackHandler) {
		this.callbackHandler = callbackHandler;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public UserPrincipal getLoginPrincipal() {
		return loginPrincipal;
	}

	public void setLoginPrincipal(UserPrincipal loginPrincipal) {
		this.loginPrincipal = loginPrincipal;
	}

	public RolePrincipal getRolePrincipal() {
		return rolePrincipal;
	}

	public void setRolePrincipal(RolePrincipal rolePrincipal) {
		this.rolePrincipal = rolePrincipal;
	}

	UserPrincipal loginPrincipal = null;
	RolePrincipal rolePrincipal = null;
	@Override
	public boolean abort() throws LoginException {
		return false;
	}

	@Override
	public boolean commit() throws LoginException {
		return true;
	}

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> arg2, Map<String, ?> arg3) {
		this.callbackHandler = callbackHandler;
		this.subject = subject;
	}

	@Override
	public boolean login() throws LoginException {
		Callback [] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("Email :");
		callbacks[1] = new PasswordCallback("Password :",false);
		try {
			callbackHandler.handle(callbacks);
			String email = ((NameCallback)callbacks[0]).getName();
			String password = String.valueOf(((PasswordCallback)callbacks[1]).getPassword());
			User user = User.searchBy("email" , email ).get(0);
			if(MD5.isEqual(user.getPassword(), password))
			{
				loginPrincipal = new UserPrincipal(user);
				rolePrincipal  = new RolePrincipal(user.getIsActive());
				subject.getPrincipals().add(loginPrincipal);
				subject.getPrincipals().add(rolePrincipal);
				User.currentUser = user;
				System.out.println("logined!!!!!!!");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedCallbackException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean logout() throws LoginException {
		subject.getPrincipals().remove(loginPrincipal);
		subject.getPrincipals().remove(rolePrincipal);
		return true;
	}

}
