package org.mamute.model;

import javax.persistence.Embeddable;

@Embeddable
public class ModerationOptions {
	static final long VISIBILITY_THRESHOLD = -5;
	private boolean invisible = false;
	
	public boolean isVisible() {
		return !invisible;
	}

	public void remove() {
		this.invisible = true;
	}
	
}
