package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.NewsInformation;
import br.com.caelum.brutal.model.User;

public class NewsDAOTest extends DatabaseTestCase {
	private User regularUser = user("Regular user", "regularuser@brutal.com");
	private User newsAuthor = user("News author", "newsauthor@brutal.com");
	private User moderator = user("News author", "newsauthor@brutal.com").asModerator();
	private NewsDAO newsForRegularUsers; 
	private NewsDAO newsForModerator; 
	
	@Before
	public void setup() {
		session.save(regularUser);
		session.save(newsAuthor);
		session.save(moderator);
		VisibleNewsFilter visibleNewsFilter = new VisibleNewsFilter();
		ModeratorOrVisibleNewsFilter moderatorOrVisible = new ModeratorOrVisibleNewsFilter(new LoggedUser(regularUser, null), visibleNewsFilter);
		newsForRegularUsers = new NewsDAO(session, moderatorOrVisible, visibleNewsFilter);
		newsForModerator = new NewsDAO(session, new ModeratorOrVisibleNewsFilter(new LoggedUser(moderator, null), visibleNewsFilter), visibleNewsFilter);
	}

	@Test
	public void should_list_all_visible_news() {
		saveNotApprovedNews(newsAuthor);
		saveApprovedNews(newsAuthor);
		
		List<News> allVisible = newsForRegularUsers.allVisible(1, 100);
		assertEquals(1, allVisible.size());
	}
	
	@Test
	public void should_list_visible_and_approved_news() {
		saveApprovedNews(newsAuthor);
		
		List<News> allVisible = newsForRegularUsers.allVisible(1, 100);
		assertEquals(1, allVisible.size());
	}
	
	@Test
	public void should_not_list_visible_and_not_approved_news() {
		saveNotApprovedNews(newsAuthor);
		
		List<News> allVisible = newsForRegularUsers.allVisible(1, 100);
		assertEquals(0, allVisible.size());
	}

	public void should_show_not_approved_news_to_moderator(){
		saveNotApprovedNews(newsAuthor);
		
		List<News> allVisible = newsForModerator.allVisible(1, 100);
		assertEquals(1, allVisible.size());
	}
	
	public void should_not_show_not_approved_news_to_moderator(){
		saveNotApprovedNews(newsAuthor);
		
		List<News> allVisible = newsForModerator.allVisibleAndApproved(1, 100);
		assertEquals(1, allVisible.size());
	}
	
	private void saveApprovedNews(User newsAuthor) {
		saveNews(newsAuthor, true);
	}

	private void saveNotApprovedNews(User newsAuthor) {
		saveNews(newsAuthor, false);
	}
	
	private void saveNews(User newsAuthor, boolean approved) {
		NewsInformation newsInformation = newsInformation(newsAuthor);
		News news = new News(newsInformation, newsAuthor);
		
		if (approved)
			news.approved();
		
		session.save(newsInformation);
		session.save(news);
	}

	private NewsInformation newsInformation(User author) {
		return new NewsInformation("breaking news breaking news",
				"breaking news breaking news breaking news breaking news", new LoggedUser(author, null),
				"comment comment comment comment");
	}

}
