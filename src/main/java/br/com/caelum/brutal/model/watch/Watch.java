package br.com.caelum.brutal.model.watch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;

@Entity
public class Watch {
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
	public Watch() {
		this(null, null);
	}
	
	public Watch(User watcher, Question watchedQuestion){
		this.watcher = watcher;
		this.watchedQuestion = watchedQuestion;
	}

	public void innactivate() {
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
