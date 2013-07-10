package br.com.caelum.brutal.auth.rules;

import br.com.caelum.brutal.model.User;

public class LoggedInRule<T> implements PermissionRule<T> {

	@Override
	public boolean isAllowed(User u, T item) {
		return u != null;
	}

}
