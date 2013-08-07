package br.com.caelum.brutal.dao;

import static br.com.caelum.brutal.newsletter.RegularUserNewsletterJob.DAY_OF_WEEK_TO_SEND;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.model.NewsletterSentLog;
import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;

public class NewsletterSentLogDAOTest extends DatabaseTestCase{

	private NewsletterSentLogDAO newsletterSentLogs;

	@Before
	public void setUp(){
		newsletterSentLogs = new NewsletterSentLogDAO(session);
	}
	
	@Test
	public void should_return_true_if_was_already_sent_this_week() {
		TimeMachine.goTo(DAY_OF_WEEK_TO_SEND).andExecute(new Block<NewsletterSentLog>() {
			@Override
			public NewsletterSentLog run() {
				newsletterSentLogs.saveLog();
				return null;
			}
		});
		
		assertTrue(newsletterSentLogs.wasSentThisWeek());
	}
	
	@Test
	public void should_return_false_if_wasnt_sent_this_week() {
		assertFalse(newsletterSentLogs.wasSentThisWeek());
	}

}
