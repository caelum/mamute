package br.com.caelum.brutal.model.flag;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Flaggable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class RemoveFlaggedByModerator implements FlagAction {
	
	private final LoggedUser loggedUser;

	public RemoveFlaggedByModerator(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
		
	}

	@Override
	public void fire(Flaggable flaggable) {
		User user = loggedUser.getCurrent();
		if (user != null && user.isModerator()) {
			flaggable.remove();
		}
	}

}
