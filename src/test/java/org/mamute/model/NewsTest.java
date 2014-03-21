package org.mamute.model;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mamute.dao.TestCase;
import org.mamute.infra.NotFoundException;
import org.mamute.model.LoggedUser;
import org.mamute.model.News;
import org.mamute.model.User;

public class NewsTest extends TestCase {
	
	private User moderator;
	private User author; 
	private User regularUser;
	
	@Before
	public void setup() {
		moderator = user("moderator", "moderator@brutal.com", 1l).asModerator();
		author = user("author", "author@brutal.com", 2l);
		regularUser = user("regular", "regular@brutal.com", 3l);
	}

	@Test
	public void not_approved_news_should_be_visible_to_moderator() {
		News news = news("news title", "news description", author);
		assertTrue(news.checkVisibilityFor(loggedUser(moderator)));
	}
	
	@Test
	public void not_approved_news_should_be_visible_to_author() {
		News news = news("news title", "news description", author);
		assertTrue(news.checkVisibilityFor(loggedUser(author)));
	}
	
	@Test(expected=NotFoundException.class)
	public void not_approved_news_should_not_be_visible_to_regular_user() {
		News news = news("news title", "news description", author);
		news.checkVisibilityFor(loggedUser(regularUser));
	}
	
	@Test
	public void approved_news_should_be_visible() {
		News news = news("news title", "news description", author).approved();
		
		news.checkVisibilityFor(loggedUser(regularUser));
		news.checkVisibilityFor(loggedUser(author));
		news.checkVisibilityFor(loggedUser(moderator));
	}

	private LoggedUser loggedUser(User author) {
		return new LoggedUser(author, null);
	}


}
