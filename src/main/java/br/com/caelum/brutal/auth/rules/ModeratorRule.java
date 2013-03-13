package br.com.caelum.brutal.auth.rules;

import br.com.caelum.brutal.model.User;

public class ModeratorRule implements PermissionRule<Void> {

	@Override
	public boolean isAllowed(User u, Void item) {
		return u.isModerator();
	}

}
