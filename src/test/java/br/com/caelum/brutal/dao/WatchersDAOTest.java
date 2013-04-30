package br.com.caelum.brutal.dao;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.watch.Watch;

public class WatchersDAOTest extends DatabaseTestCase{

	private WatchersDAO watchers;
	private Question question;


	@Before
	public void set_up(){
		User leo = user("Leonardo", "leo@leo.com");
		session.save(leo);
		Tag java = tag("java");
		session.save(java);
		question = question(leo, java);
		session.save(question);
		watchers = new WatchersDAO(session);
	}
	
	@Test
	public void should_get_subscribed_users_of_a_question() {
		User subscribedWatcher = user("watcher", "watcher@watcher.com");
		session.save(subscribedWatcher);
		watchers.add(new Watch(subscribedWatcher, question));
		
		assertThat(watchers.of(question), not(empty()));
	}
	
	@Test
	public void should_not_get_unsubscribed_users_of_a_question() {
		User unsubscribedWatcher = user("watcher", "watcher@watcher.com");
		unsubscribedWatcher.setSubscribed(false);
		session.save(unsubscribedWatcher);

		watchers.add(new Watch(unsubscribedWatcher, question));
		
		assertThat(watchers.of(question), empty());
	}
	
	@Test
	public void should_not_get_innactive_watchers_of_a_question() {
		User subscribedWatcher = user("watcher", "watcher@watcher.com");
		session.save(subscribedWatcher);
		
		Watch watch = new Watch(subscribedWatcher, question);
		watch.innactivate();
		watchers.add(watch);
		
		assertThat(watchers.of(question), empty());
	}
	
	@Test
	public void should_not_get_not_watchers_of_a_question() {
		assertThat(watchers.of(question), empty());
	}

}
