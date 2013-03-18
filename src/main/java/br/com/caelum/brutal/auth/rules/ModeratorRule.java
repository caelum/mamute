package br.com.caelum.brutal.auth.rules;

import br.com.caelum.brutal.model.User;

public class ModeratorRule<T> implements PermissionRule<T> {

	@Override
	public boolean isAllowed(User u, T item) {
		if(u == null) return false;
		return u.isModerator();
	}

}
