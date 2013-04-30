package br.com.caelum.brutal.notification;

import br.com.caelum.brutal.mail.action.EmailAction;
import br.com.caelum.brutal.model.User;

public class NotificationMail {
	
	private final User watcher;
	private final EmailAction emailAction;

	public NotificationMail(EmailAction emailAction, User watcher) {
		this.emailAction = emailAction;
		this.watcher = watcher;
		
	}

	public EmailAction getAction() {
		return emailAction;
	}
	
	public User getTo() {
		return watcher;
	}

	public String getEmailTemplate() {
		return emailAction.getWhat().getEmailTemplate();
	}
}
