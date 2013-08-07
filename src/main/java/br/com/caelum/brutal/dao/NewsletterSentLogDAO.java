package br.com.caelum.brutal.dao;

import static br.com.caelum.brutal.newsletter.RegularUserNewsletterJob.DAY_OF_WEEK_TO_SEND;

import org.hibernate.Session;

import br.com.caelum.brutal.model.NewsletterSentLog;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class NewsletterSentLogDAO {
	private final Session session;

	public NewsletterSentLogDAO(Session session) {
		this.session = session;
	}
	
	public boolean wasSentThisWeek(){
		return session.createQuery("from NewsletterSentLog n where year(createdAt) = :thisWeekYear" +
				" and month(createdAt) = :thisWeekMonth" +
				" and day(createdAt) = :thisWeekDay")
			   .setParameter("thisWeekYear", DAY_OF_WEEK_TO_SEND.getYear())
			   .setParameter("thisWeekMonth", DAY_OF_WEEK_TO_SEND.getMonthOfYear())
			   .setParameter("thisWeekDay", DAY_OF_WEEK_TO_SEND.getDayOfMonth())
			   .uniqueResult() != null;
	}

	public void saveLog() {
		session.save(new NewsletterSentLog());
	}
}
