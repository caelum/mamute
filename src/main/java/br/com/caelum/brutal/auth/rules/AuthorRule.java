package br.com.caelum.brutal.auth.rules;

import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;


public class AuthorRule<T extends Moderatable> implements PermissionRule<Moderatable> {

	@Override
	public boolean isAllowed(User u, Moderatable item) {
		if (item.getAuthor() == null) {
			throw new IllegalArgumentException("can't verify permissions on item without an author");
		}
		return item.getAuthor().getId() == u.getId();
	}

}
