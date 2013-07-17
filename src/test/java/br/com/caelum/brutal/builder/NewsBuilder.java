package br.com.caelum.brutal.builder;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.NewsInformation;
import br.com.caelum.brutal.model.User;

public class NewsBuilder extends ModelBuilder {
	private String title = "default news";
	private String description = "default news default news default news";
	private String comment = "blablaba";
	private User author = new User("author", "newsauthor@gmail.com");
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
