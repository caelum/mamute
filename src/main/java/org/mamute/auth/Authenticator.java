package org.mamute.auth;

/**
 * Authentication provider for Mamute
 */
public interface Authenticator {
	/**
	 * Perform a simple authentication with the provided credentials, returning
	 * a simple boolean to indicate whether it was successful or not. If there
	 * are any exceptional conditions, the implementation will throw an
	 * {@link org.mamute.auth.AuthenticationException}.
	 *
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean authenticate(String user, String password);

	/**
	 * Performs a  logout. The implementation should keep track of the current
	 * user in order to perform this action.
	 */
	public void signout();
}
