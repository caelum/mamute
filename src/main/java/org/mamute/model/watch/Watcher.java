package org.mamute.model.watch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.mamute.model.User;
import org.mamute.providers.SessionFactoryCreator;

@Entity
public class Watcher {
	@GeneratedValue @Id
	private Long id;
	
	private boolean active = true;
	
	@ManyToOne
	private final User watcher;

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt;

	/**
	 * @deprecated hibernate eyes only
	 */
	public Watcher() {
		this(null);
	}
	
	public Watcher(User watcher){
		this.watcher = watcher;
		this.createdAt = new DateTime();
	}

	public void inactivate() {
		active = false;
	}

	public void activate() {
		active = true;
	}

	public boolean isActive() {
		return active;
	}

	public User getWatcher() {
		return watcher;
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		Watcher other = (Watcher) obj;
		if(id == null || other.id == null) return false;
		if (id.equals(other.id))
			return true;
		return false;
	}

	public Long getId() {
		return this.id;
	}

}
