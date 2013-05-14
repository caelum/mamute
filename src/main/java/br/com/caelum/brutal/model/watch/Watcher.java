package br.com.caelum.brutal.model.watch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;

@Entity
public class Watcher {
	@GeneratedValue @Id
	private Long id;
	
	private boolean active = true;
	
	@ManyToOne
	private final User watcher;

	@ManyToOne
	private final Question watchedQuestion;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt;

	/**
	 * @deprecated hibernate eyes only
	 */
	public Watcher() {
		this(null, null);
	}
	
	public Watcher(User watcher, Question watchedQuestion){
		this.watcher = watcher;
		this.watchedQuestion = watchedQuestion;
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

	public Question getWatchedQuestion() {
		return watchedQuestion;
	}
	
	public DateTime getCreatedAt() {
		return createdAt;
	}

}
