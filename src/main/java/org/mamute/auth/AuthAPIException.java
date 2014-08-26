package org.mamute.auth;


public class AuthAPIException extends RuntimeException {
	private String authType;

	public AuthAPIException(String authType, String msg, Throwable t) {
		super(msg, t);
		this.authType = authType;
	}

	public String getAuthType() {
		return authType;
	}
}
