package org.mamute.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mamute.model.MarkedText.notMarked;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mamute.model.LoggedUser;
import org.mamute.model.News;
import org.mamute.model.NewsInformation;
import org.mamute.model.User;

import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;

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

	@Test
	public void should_show_not_approved_news_to_moderator(){
		saveNotApprovedNews(newsAuthor);
		
		List<News> allVisible = newsForModerator.allVisible(1, 100);
		assertEquals(1, allVisible.size());
	}
	
	@Test
	public void should_get_the_last_5_news_ordered_by_date(){
		News thirdNews = saveApprovedNews(newsAuthor);
		News secondNews = createNewsAt(new DateTime().plusDays(1));
		News firstNews = createNewsAt(new DateTime().plusDays(2));
		
		List<News> newses = newsForModerator.hotNews();
		assertEquals(firstNews.getId(), newses.get(0).getId());
		assertEquals(secondNews.getId(), newses.get(1).getId());
		assertEquals(thirdNews.getId(), newses.get(2).getId());
	}
	
	@Test
	public void should_get_the_last_5(){
		saveApprovedNews(newsAuthor);
		saveApprovedNews(newsAuthor);
		saveApprovedNews(newsAuthor);
		saveApprovedNews(newsAuthor);
		saveApprovedNews(newsAuthor);
		saveApprovedNews(newsAuthor);
		
		List<News> newses = newsForModerator.hotNews();
		assertEquals(5, newses.size());
	}
	
	@Test
	public void should_get_only_approved_news(){
		saveApprovedNews(newsAuthor);
		saveNotApprovedNews(newsAuthor);
		
		List<News> newses = newsForModerator.hotNews();
		assertEquals(1, newses.size());
		assertTrue(newses.get(0).isApproved());
	}

	private News createNewsAt(DateTime date) {
		return TimeMachine.goTo(date).andExecute(new Block<News>() {
			@Override
			public News run() {
				News secondNews = saveApprovedNews(newsAuthor);
				return secondNews;
			}
			
		});
	}
	
	private News saveApprovedNews(User newsAuthor) {
		return saveNews(newsAuthor, true);
	}

	private News saveNotApprovedNews(User newsAuthor) {
		return saveNews(newsAuthor, false);
	}
	
	private News saveNews(User newsAuthor, boolean approved) {
		NewsInformation newsInformation = newsInformation(newsAuthor);
		News news = new News(newsInformation, newsAuthor);
		
		if (approved)
			news.approved();
		
		session.save(newsInformation);
		session.save(news);
		return news;
	}

	private NewsInformation newsInformation(User author) {
		return new NewsInformation("breaking news breaking news",
				notMarked("breaking news breaking news breaking news breaking news"), new LoggedUser(author, null),
				"comment comment comment comment");
	}

}
