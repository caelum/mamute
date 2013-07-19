package br.com.caelum.brutal.mail.action;

import br.com.caelum.brutal.model.Post;
import br.com.caelum.brutal.model.interfaces.Notifiable;
import br.com.caelum.brutal.model.interfaces.Watchable;

public class EmailAction {
	private final Notifiable notifiable;
	private final Post post;

	public EmailAction(Notifiable notifiable, Post post) {
		this.notifiable = notifiable;
		this.post = post;
	}

	public Notifiable getWhat() {
		return notifiable;
	}

	public Post getWhere() {
		return post;
	}

	public Watchable getMainThread() {
		return post.getMainThread();
	}

}
