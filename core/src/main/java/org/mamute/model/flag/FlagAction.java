package org.mamute.model.flag;

import org.mamute.model.interfaces.Flaggable;

public interface FlagAction {

	void fire(Flaggable flaggable);

	boolean shouldHandle(Flaggable flaggable);

}
