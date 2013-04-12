package br.com.caelum.brutal.model;

import javax.persistence.Embeddable;

@Embeddable
public class ModerationOptions {
	private boolean invisible = false;
	
	public boolean isInvisible() {
		return invisible;
	}

	public void remove() {
		this.invisible = true;
	}
}
