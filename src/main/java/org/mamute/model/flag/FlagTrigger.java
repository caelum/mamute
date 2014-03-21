package org.mamute.model.flag;

import java.util.List;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.mamute.model.interfaces.Flaggable;

import com.google.common.collect.Lists;

public class FlagTrigger {
	
	private List<FlagAction> actions;

	@Deprecated
	public FlagTrigger() {
	}
	
	@Inject
	public FlagTrigger(@Any Instance<FlagAction> actions) {
		this((Iterable<FlagAction>)actions);
	}
	
	public FlagTrigger(Iterable<FlagAction> actions) {
		this.actions = Lists.newArrayList(actions);
	}

	public void fire(Flaggable flaggable) {
		for (FlagAction action : actions) {
			if (action.shouldHandle(flaggable)) {
				action.fire(flaggable);
			}
		}
	}

}
