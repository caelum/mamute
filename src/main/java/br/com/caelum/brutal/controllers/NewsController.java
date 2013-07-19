package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.LoggedAccess;
import br.com.caelum.brutal.auth.ModeratorOnly;
import br.com.caelum.brutal.dao.NewsDAO;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.NewsInformation;
import br.com.caelum.brutal.model.PostViewCounter;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.util.hibernate.extra.Load;

@Resource
public class NewsController {

	private final LoggedUser currentUser;
	private final NewsDAO newses;
	private final Result result;
	private final VoteDAO votes;
	private PostViewCounter viewCounter;
	private final WatcherDAO watchers;

	public NewsController(LoggedUser currentUser, NewsDAO newses, Result result, VoteDAO votes, PostViewCounter viewCounter,
			WatcherDAO watchers) {
		this.currentUser = currentUser;
		this.newses = newses;
		this.result = result;
		this.votes = votes;
		this.viewCounter = viewCounter;
		this.watchers = watchers;
	}
	
	@Post("/nova-noticia")
	@LoggedAccess
	public void newNews(String title, String description) {
		NewsInformation information = new NewsInformation(title, description, currentUser, "new news");
		User author = currentUser.getCurrent();
		News news = new News(information, author);
		result.include("news", news);
		newses.save(news);
		result.redirectTo(this).showNews(news, news.getSluggedTitle());

	}
	
	@Get("/nova-noticia")
	@LoggedAccess
	public void newsForm() {
	}
	
	@Get("/editar/{news.id}")
	@LoggedAccess
	public void newsEditForm() {
	}
	
	@Get("/noticias/{news.id:[0-9]+}-{sluggedTitle}")
	public void showNews(@Load News news, String sluggedTitle) {
		User current = currentUser.getCurrent();
		news.checkVisibilityFor(currentUser);
		redirectToRightUrl(news, sluggedTitle);
		viewCounter.ping(news);
		boolean isWatching = watchers.ping(news, current);
		
		result.include("commentsWithVotes", votes.previousVotesForComments(news, current));
		result.include("currentVote", votes.previousVoteFor(news.getId(), current, News.class));
		result.include("news", news);
		result.include("isWatching", isWatching);
		result.include("userMediumPhoto", true);
	}
	
	@Post("/noticias/aprovar/{news.id}")
	@ModeratorOnly
	public void approve(@Load News news){
		news.approved();
		result.nothing();
	}

	private void redirectToRightUrl(News news, String sluggedTitle) {
		if (!news.getSluggedTitle().equals(sluggedTitle)) {
			result.redirectTo(this).showNews(news, news.getSluggedTitle());
			return;
		}
	}
}
