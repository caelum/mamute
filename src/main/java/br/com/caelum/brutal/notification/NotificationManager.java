package br.com.caelum.brutal.notification;

import java.util.List;

import br.com.caelum.brutal.dao.WatchDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.interfaces.Notifiable;
import br.com.caelum.brutal.model.watch.Watch;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class NotificationManager {

	private final WatchDAO watchers;
	private final NotificationMailer mailer;

	public NotificationManager(WatchDAO watchers, NotificationMailer notificationMailer) {
		this.watchers = watchers;
		this.mailer = notificationMailer;
	}
	
	public void sendEmailsAndActivate(Question question, Notifiable notifiable) {
		List<Watch> watchList = watchers.of(question);
		for (Watch watch : watchList) {
			mailer.send(new NotificationMail(question, notifiable, watch.getWatcher()));
			watch.activate();
		}
		
	}

}
