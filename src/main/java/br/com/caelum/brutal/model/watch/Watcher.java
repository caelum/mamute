package br.com.caelum.brutal.model.watch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

	/**
	 * @deprecated hibernate eyes only
	 */
	public Watcher() {
		this(null, null);
	}
	
	public Watcher(User watcher, Question watchedQuestion){
		this.watcher = watcher;
		this.watchedQuestion = watchedQuestion;
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

}
