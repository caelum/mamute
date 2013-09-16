package br.com.caelum.brutal.notification;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import br.com.caelum.brutal.components.CDIFakeRequestProvider;
import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.infra.AfterSuccessfulTransaction;
import br.com.caelum.brutal.infra.ThreadPoolContainer;
import br.com.caelum.brutal.mail.action.EmailAction;
import br.com.caelum.brutal.model.interfaces.Watchable;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.vraptor.core.Execution;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.errormail.mail.ErrorMail;
import br.com.caelum.vraptor.errormail.mail.ErrorMailFactory;
import br.com.caelum.vraptor.errormail.mail.ErrorMailer;
import br.com.caelum.vraptor.ioc.Container;

public class NotificationManager {
	private final static Logger LOG = Logger.getLogger(NotificationManager.class);

	@Inject private WatcherDAO watchers;
	@Inject private NotificationMailer mailer;
	@Inject private ThreadPoolContainer threadPoolContainer;
	@Inject private AfterSuccessfulTransaction afterTransaction;
	@Inject private ErrorMailer errorMailer;
	@Inject private HttpServletRequest req;
	@Inject private Environment env;
	
	@Inject	private CDIFakeRequestProvider fakeProvider;

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
		
		threadPoolContainer.execute(new SendNotifications(mails));
	}
	 
	private class SendNotifications implements Runnable{
		private List<NotificationMail> mails;

		public SendNotifications(List<NotificationMail> mails) {
			this.mails = mails;
		}

		@Override
		public void run() {
			fakeProvider.insideRequest(new Execution<Void>() {
				
				@Override
				public Void insideRequest(Container container) {
					for (NotificationMail notificationMail : mails) {
						try {
							LOG.info("Sending email: " + notificationMail);
							mailer.send(notificationMail);
						} catch (Exception e) {
							LOG.error("Could not send email: " + notificationMail, e);
							sendErrorMail(e);
						}
					}
					return null;
				}

				private void sendErrorMail(Exception exception) {
					try {
						req.setAttribute(ErrorMailFactory.EXCEPTION, exception);
						ErrorMail errorMail = new ErrorMailFactory(req, env).build();
						errorMailer.register(errorMail);
					} catch (EmailException e) {
						LOG.error("Could not send message error: ", e);
					}
				}
			});
		}		
	}
}
