package br.com.caelum.brutal.model.flag;

import java.util.List;

import br.com.caelum.brutal.model.flag.FlagAction;
import br.com.caelum.brutal.model.interfaces.Flaggable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class FlagTrigger {
	
	private final List<FlagAction> actions;

	public FlagTrigger(List<FlagAction> actions) {
		this.actions = actions;
		
	}

	public void fire(Flaggable flaggable) {
		for (FlagAction action : actions) {
			action.fire(flaggable);
		}
	}

}
