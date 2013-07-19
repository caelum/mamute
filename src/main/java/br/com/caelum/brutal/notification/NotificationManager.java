package br.com.caelum.brutal.notification;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.infra.AfterSuccessfulTransaction;
import br.com.caelum.brutal.infra.ThreadPoolContainer;
import br.com.caelum.brutal.mail.action.EmailAction;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.interfaces.Watchable;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class NotificationManager {

	private final WatcherDAO watchers;
	private final NotificationMailer mailer;
	private final ThreadPoolContainer threadPoolContainer;
	private final static Logger LOG = Logger.getLogger(NotificationManager.class);
	private final AfterSuccessfulTransaction afterTransaction; 

	public NotificationManager(WatcherDAO watchers, NotificationMailer notificationMailer, 
			ThreadPoolContainer threadPoolContainer, AfterSuccessfulTransaction afterTransaction) {
		this.watchers = watchers;
		this.mailer = notificationMailer;
		this.threadPoolContainer = threadPoolContainer;
		this.afterTransaction = afterTransaction;
	}
	
	public void sendEmailsAndInactivate(EmailAction emailAction) {
		Watchable watchable = emailAction.getMainThread();
		List<Watcher> watchList = watchers.of(watchable);
		
		final List<NotificationMail> mails = buildMails(emailAction, watchList);
		afterTransaction.execute(new Runnable() {
			@Override
			public void run() {
				NotificationManager.this.sendMailsAsynchronously(mails);
			}
		});
	}

	private List<NotificationMail> buildMails(EmailAction emailAction, List<Watcher> watchList) {
		List<NotificationMail> mails = new ArrayList<NotificationMail>();
		for (Watcher watcher : watchList) {
			boolean sameAuthor = watcher.getWatcher().getId().equals(emailAction.getWhat().getAuthor().getId());
			if (!sameAuthor) {
				mails.add(new NotificationMail(emailAction, watcher.getWatcher()));
				watcher.inactivate();
			}
		}
		return mails;
	}

	private void sendMailsAsynchronously(final List<NotificationMail> mails) {
		threadPoolContainer.execute(new Runnable() {
			@Override
			public void run() {
				for (NotificationMail notificationMail : mails) {
					try {
						LOG.info("Sending email: " + notificationMail);
						mailer.send(notificationMail);
					} catch (Exception e) {
						LOG.error("Could not send email: " + notificationMail, e);
					}
				}
			}
		});
	}

}
