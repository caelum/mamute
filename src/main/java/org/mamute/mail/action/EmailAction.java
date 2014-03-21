package org.mamute.mail.action;

import org.mamute.model.Post;
import org.mamute.model.interfaces.Notifiable;
import org.mamute.model.interfaces.Watchable;

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
