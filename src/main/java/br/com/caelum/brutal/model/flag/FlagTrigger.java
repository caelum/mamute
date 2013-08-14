package br.com.caelum.brutal.model.flag;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.brutal.model.interfaces.Flaggable;

public class FlagTrigger {
	
	private List<FlagAction> actions;

	@Deprecated
	public FlagTrigger() {
	}
	
	@Inject
	public FlagTrigger(List<FlagAction> actions) {
		this.actions = actions;
	}

	public void fire(Flaggable flaggable) {
		for (FlagAction action : actions) {
			if (action.shouldHandle(flaggable)) {
				action.fire(flaggable);
			}
		}
	}

}
