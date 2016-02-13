package org.mamute.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.mamute.providers.SessionFactoryCreator;

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

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private DateTime moderatedAt = new DateTime();

	@ManyToOne(optional = true)
	private User moderatedBy;
	
	public DateTime getModeratedAt() {
		return moderatedAt;
	}

}
