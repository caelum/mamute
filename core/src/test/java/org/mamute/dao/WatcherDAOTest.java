package org.mamute.dao;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mamute.dao.InvisibleForUsersRule;
import org.mamute.dao.WatcherDAO;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.mamute.model.User;
import org.mamute.model.watch.Watcher;

public class WatcherDAOTest extends DatabaseTestCase{

	private WatcherDAO watchers;
	private Question question;

	@Before
	public void set_up(){
		User leo = user("Leonardo", "leo@leo.com");
		session.save(leo);
		Tag java = tag("java");
		session.save(java);
		question = question(leo, java);
		session.save(question);
		watchers = new WatcherDAO(session, new InvisibleForUsersRule(new LoggedUser(leo, null)));
	}
	
	@Test
	public void should_get_subscribed_users_of_a_question() {
		User subscribedWatcher = user("watcher", "watcher@watcher.com");
		session.save(subscribedWatcher);
		watchers.add(question, new Watcher(subscribedWatcher));
		
		assertThat(watchers.of(question), not(empty()));
	}
	
	@Test
	public void should_not_get_innactive_watchers_of_a_question() {
		User subscribedWatcher = user("watcher", "watcher@watcher.com");
		session.save(subscribedWatcher);
		
		Watcher watch = new Watcher(subscribedWatcher);
		watch.inactivate();
		watchers.add(question, watch);
		
		assertThat(watchers.of(question), empty());
	}
	
	@Test
	public void should_not_get_not_watchers_of_a_question() {
		assertThat(watchers.of(question), empty());
	}
	
	@Test
	public void should_innactivate_watcher() {
		User subscribedWatcher = user("watcher", "watcher@watcher.com");
		session.save(subscribedWatcher);
		Watcher watch = new Watcher(subscribedWatcher);
		watch.inactivate();
		watchers.add(question, watch);
		
		watchers.ping(question, subscribedWatcher);
		
		assertTrue(watch.isActive());
	}

}
