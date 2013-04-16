package br.com.caelum.brutal.model;

import javax.persistence.Embeddable;

@Embeddable
public class ModerationOptions {
	static final long VISIBILITY_THRESHOLD = -5;
	private boolean invisible = false;
	
	public boolean isInvisible() {
		return invisible;
	}

	public void remove() {
		this.invisible = true;
	}
	
}
