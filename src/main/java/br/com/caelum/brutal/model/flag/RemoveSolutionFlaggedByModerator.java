package br.com.caelum.brutal.model.flag;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.interfaces.Flaggable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class RemoveSolutionFlaggedByModerator implements FlagAction {
	
	private final LoggedUser loggedUser;

	public RemoveSolutionFlaggedByModerator(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}

	@Override
	public void fire(Flaggable flaggable) {
		Answer answer = (Answer) flaggable;
		answer.removeSolution();
	}
	
	@Override
	public boolean shouldHandle(Flaggable flaggable) {
		boolean isQuestion = Answer.class.isAssignableFrom(flaggable.getClass());
		return isQuestion && loggedUser.isLoggedIn() && loggedUser.isModerator();
	}

}
