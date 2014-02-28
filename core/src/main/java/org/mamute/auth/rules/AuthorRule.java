package org.mamute.auth.rules;

import org.mamute.model.User;
import org.mamute.model.interfaces.Moderatable;


public class AuthorRule<T extends Moderatable> implements PermissionRule<Moderatable> {

	@Override
	public boolean isAllowed(User u, Moderatable item) {
		if (u == null) 
			return false;
		if (item.getAuthor() == null) {
			throw new IllegalArgumentException("can't verify permissions on item without an author");
		}
		return item.getAuthor().getId().equals(u.getId());
	}

}
