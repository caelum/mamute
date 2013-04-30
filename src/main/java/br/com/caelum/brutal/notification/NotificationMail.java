package br.com.caelum.brutal.notification;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Notifiable;

public class NotificationMail {
	
	private final Question question;
	private final Notifiable notifiable;
	private final User watcher;

	public NotificationMail(Question question, Notifiable notifiable, User watcher) {
		this.question = question;
		this.notifiable = notifiable;
		this.watcher = watcher;
	}

	public Question getQuestion() {
		return question;
	}

	public User getTo() {
		return watcher;
	}

	public String getEmailTemplate() {
		return notifiable.getEmailTemplate();
	}
}
