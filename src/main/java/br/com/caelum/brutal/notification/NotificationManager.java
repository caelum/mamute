package br.com.caelum.brutal.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.jboss.weld.context.bound.BoundRequestContext;
import org.jboss.weld.context.bound.BoundSessionContext;

import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.infra.AfterSuccessfulTransaction;
import br.com.caelum.brutal.infra.ThreadPoolContainer;
import br.com.caelum.brutal.mail.action.EmailAction;
import br.com.caelum.brutal.model.interfaces.Watchable;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.vraptor4.core.RequestInfo;

public class NotificationManager {
	private final static Logger LOG = Logger.getLogger(NotificationManager.class);

	@Inject private WatcherDAO watchers;
	@Inject private NotificationMailer mailer;
	@Inject private ThreadPoolContainer threadPoolContainer;
	@Inject private AfterSuccessfulTransaction afterTransaction;
	
	
	@Inject	private BoundRequestContext requestContext;
	@Inject	private BoundSessionContext sessionContext;
	@Inject private RequestInfo requestInfo;
	@Inject	private BeanManager beanManager;

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
				
				Map<String, Object> requestDataStore = Collections
						.synchronizedMap(new HashMap<String, Object>());
				requestContext.associate(requestDataStore);
				requestContext.activate();
				
				Map<String, Object> sessionDataStore = Collections
						.synchronizedMap(new HashMap<String, Object>());
				sessionContext.associate(sessionDataStore);
				sessionContext.activate();
				
				beanManager.fireEvent(requestInfo);
				
				for (NotificationMail notificationMail : mails) {
					try {
						LOG.info("Sending email: " + notificationMail);
						mailer.send(notificationMail);
					} catch (Exception e) {
						LOG.error("Could not send email: " + notificationMail, e);
					}
				}
				
				requestContext.invalidate();
				requestContext.deactivate();
				requestContext.dissociate(requestDataStore);
				sessionContext.invalidate();
				sessionContext.deactivate();
				sessionContext.dissociate(sessionDataStore);
			}
		});
	}

}
