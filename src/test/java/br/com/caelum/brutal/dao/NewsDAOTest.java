package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.NewsInformation;
import br.com.caelum.brutal.model.User;

public class NewsDAOTest extends DatabaseTestCase {

	@Test
	public void should_list_visible_news() {
		User regularUser = user("Regular user", "regularuser@brutal.com");
		User newsAuthor = user("News author", "newsauthor@brutal.com");
		LoggedUser user = new LoggedUser(regularUser, null);
		InvisibleForUsersRule invisible = new InvisibleForUsersRule(user);
		NewsInformation newsInformation = newsInformation(newsAuthor);
		News news = new News(newsInformation, newsAuthor);
		
		session.save(regularUser);
		session.save(newsAuthor);
		session.save(newsInformation);
		session.save(news);
		
		NewsDAO newsDAO = new NewsDAO(session, invisible);
		List<News> allVisible = newsDAO.allVisible(1, 100);
		assertEquals(1, allVisible.size());

	}

	private NewsInformation newsInformation(User author) {
		return new NewsInformation("breaking news breaking news",
				"breaking news breaking news breaking news breaking news", new LoggedUser(author, null),
				"comment comment comment comment");
	}

}
