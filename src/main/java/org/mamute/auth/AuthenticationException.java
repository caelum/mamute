package org.mamute.auth;


public class AuthenticationException extends RuntimeException {
	private String authType;

	public AuthenticationException(String authType, String msg, Throwable t) {
		super(msg, t);
		this.authType = authType;
	}

	public String getAuthType() {
		return authType;
	}
}
