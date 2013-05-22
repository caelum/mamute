package br.com.caelum.brutal.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Embeddable
public class Moderation {

	/**
	 * @deprecated hibernate only
	 */
	protected Moderation() {
		
	}
	public Moderation(User moderator) {
		this.moderatedBy = moderator;
	}

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime moderatedAt = new DateTime();

	@ManyToOne(optional = true)
	private User moderatedBy;
	
	public DateTime getModeratedAt() {
		return moderatedAt;
	}

}
