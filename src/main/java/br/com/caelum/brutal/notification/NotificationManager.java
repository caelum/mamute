package br.com.caelum.brutal.notification;

import java.util.List;

import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.mail.action.EmailAction;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class NotificationManager {

	private final WatcherDAO watchers;
	private final NotificationMailer mailer;

	public NotificationManager(WatcherDAO watchers, NotificationMailer notificationMailer) {
		this.watchers = watchers;
		this.mailer = notificationMailer;
	}
	
	public void sendEmailsAndInactivate(EmailAction emailAction) {
		Question question = emailAction.getQuestion();
		List<Watcher> watchList = watchers.of(question);
		for (Watcher watcher : watchList) {
			mailer.send(new NotificationMail(emailAction, watcher.getWatcher()));
			watcher.inactivate();
		}
		
	}

}
