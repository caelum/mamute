package br.com.caelum.brutal.model.flag;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.interfaces.Flaggable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class RemoveAnythingFlaggedByModerator implements FlagAction {
	
	private final LoggedUser loggedUser;

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
