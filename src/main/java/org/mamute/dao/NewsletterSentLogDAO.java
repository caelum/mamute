package org.mamute.dao;

import javax.inject.Inject;

import org.hibernate.Session;
import org.joda.time.DateTime;
import org.mamute.model.NewsletterSentLog;

public class NewsletterSentLogDAO {
	private Session session;

	@Deprecated
	public NewsletterSentLogDAO() {	}
	
	@Inject
	public NewsletterSentLogDAO(Session session) {
		this.session = session;
	}
	
	public boolean wasSentThisWeek(){
		return session.createQuery("from NewsletterSentLog n where createdAt between :firstDay and :lastDay")
			   .setParameter("firstDay", new DateTime().withDayOfWeek(1).minusDays(1))
			   .setParameter("lastDay", new DateTime().withDayOfWeek(7).withHourOfDay(23).withMinuteOfHour(59))
			   .uniqueResult() != null;
	}

	public void saveLog() {
		session.save(new NewsletterSentLog());
	}
}
