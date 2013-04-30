package br.com.caelum.brutal.notification;

import java.util.List;

import br.com.caelum.brutal.dao.WatchersDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Notifiable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class NotificationManager {

	private final WatchersDAO watchers;
	private final NotificationMailer mailer;

	public NotificationManager(WatchersDAO watchers, NotificationMailer notificationMailer) {
		this.watchers = watchers;
		this.mailer = notificationMailer;
	}
	
	public void sendEmailsFor(Question question, Notifiable notifiable) {
		List<User> watcherList = watchers.of(question);
		for (User watcher : watcherList) {
			mailer.send(new NotificationMail(question, notifiable, watcher));
		}
		
	}

}
