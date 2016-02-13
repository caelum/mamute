package org.mamute.model.flag;

import javax.inject.Inject;

import org.mamute.model.LoggedUser;
import org.mamute.model.interfaces.Flaggable;

public class RemoveAnythingFlaggedByModerator implements FlagAction {

	private LoggedUser loggedUser;
	
	@Deprecated
	public RemoveAnythingFlaggedByModerator() {
	}

	@Inject
	public RemoveAnythingFlaggedByModerator(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}

	@Override
	public void fire(Flaggable flaggable) {
		flaggable.remove();
	}

	@Override
	public boolean shouldHandle(Flaggable flaggable) {
		return loggedUser.isLoggedIn() && loggedUser.isModerator();
	}

}
