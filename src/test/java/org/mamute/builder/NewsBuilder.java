package org.mamute.builder;


import org.mamute.model.LoggedUser;
import org.mamute.model.MarkedText;
import org.mamute.model.News;
import org.mamute.model.NewsInformation;
import org.mamute.model.SanitizedText;
import org.mamute.model.User;

public class NewsBuilder extends ModelBuilder {
	private String title = "default news";
	private MarkedText description = MarkedText.notMarked("default news default news default news");
	private String comment = "blablaba";
	private User author = new User(SanitizedText.fromTrustedText("author"), "newsauthor@gmail.com");
	private LoggedUser loggedUser = null;
	private Long id = null;
	
	public News build() {
		if (loggedUser == null) {
			loggedUser = new LoggedUser(author, null);
		}
		NewsInformation newsInformation = new NewsInformation(title, description, loggedUser, comment);
		News news = new News(newsInformation, author);
		setId(news, id);
		return news;
	}

	public NewsBuilder withId(long id) {
		this.id = id;
		return this;
	}
	

}
