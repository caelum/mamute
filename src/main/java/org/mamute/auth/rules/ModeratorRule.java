package org.mamute.auth.rules;

import org.mamute.model.User;

public class ModeratorRule<T> implements PermissionRule<T> {

	@Override
	public boolean isAllowed(User u, T item) {
		return u != null && u.isModerator();
	}

}
