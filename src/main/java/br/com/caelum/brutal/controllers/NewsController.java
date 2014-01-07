package br.com.caelum.brutal.controllers;

import javax.inject.Inject;

import br.com.caelum.brutal.brutauth.auth.rules.EditNewsRule;
import br.com.caelum.brutal.brutauth.auth.rules.InputRule;
import br.com.caelum.brutal.brutauth.auth.rules.LoggedRule;
import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOnlyRule;
import br.com.caelum.brutal.dao.NewsDAO;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.NewsInformation;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.post.PostViewCounter;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.hibernate.extra.Load;

@Controller
public class NewsController {

	@Inject private LoggedUser currentUser;
	@Inject private NewsDAO newses;
	@Inject private Result result;
	@Inject private VoteDAO votes;
	@Inject private PostViewCounter viewCounter;
	@Inject private WatcherDAO watchers;

	@Post("/nova-noticia")
	@CustomBrutauthRules({LoggedRule.class, InputRule.class})
	public void newNews(String title, String description) {
		NewsInformation information = new NewsInformation(title, description, currentUser, "new news");
		User author = currentUser.getCurrent();
		News news = new News(information, author);
		result.include("news", news);
		newses.save(news);
		result.redirectTo(this).showNews(news, news.getSluggedTitle());
	}
	
	@Get("/nova-noticia")
	@CustomBrutauthRules({LoggedRule.class, InputRule.class})
	public void newsForm() {
		result.include("newsActive", true);
		result.include("noDefaultActive", true);
	}
	
	@Get("/noticia/editar/{news.id}")
	@CustomBrutauthRules(EditNewsRule.class)
	public void newsEditForm(@Load News news) {
		result.include("news", news);
		result.include("newsActive", true);
		result.include("noDefaultActive", true);
	}
	
	@Post("/noticia/editar/{news.id}")
	@CustomBrutauthRules(EditNewsRule.class)
	public void saveEdit(@Load News news, String title, String description, String comment) {
		Information newInformation = new NewsInformation(title, description, currentUser, comment);
		news.enqueueChange(newInformation, UpdateStatus.NO_NEED_TO_APPROVE);
		result.redirectTo(this).showNews(news, news.getSluggedTitle());
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
		result.include("newsActive", true);
		result.include("noDefaultActive", true);
	}
	
	@Post("/noticias/aprovar/{news.id}")
	@CustomBrutauthRules(ModeratorOnlyRule.class)
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
