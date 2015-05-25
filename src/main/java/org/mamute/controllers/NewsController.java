package org.mamute.controllers;

import javax.inject.Inject;

import org.mamute.brutauth.auth.rules.*;
import org.mamute.dao.NewsDAO;
import org.mamute.dao.VoteDAO;
import org.mamute.dao.WatcherDAO;
import org.mamute.model.Information;
import org.mamute.model.LoggedUser;
import org.mamute.model.MarkedText;
import org.mamute.model.News;
import org.mamute.model.NewsInformation;
import org.mamute.model.UpdateStatus;
import org.mamute.model.User;
import org.mamute.model.post.PostViewCounter;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.hibernate.extra.Load;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class NewsController {

	@Inject private LoggedUser currentUser;
	@Inject private NewsDAO newses;
	@Inject private Result result;
	@Inject private VoteDAO votes;
	@Inject private PostViewCounter viewCounter;
	@Inject private WatcherDAO watchers;

	@Post
	@CustomBrutauthRules({LoggedRule.class, InputRule.class})
	public void newNews(String title, MarkedText description) {
		NewsInformation information = new NewsInformation(title, description, currentUser, "new news");
		User author = currentUser.getCurrent();
		News news = new News(information, author);
		result.include("news", news);
		newses.save(news);
		result.redirectTo(this).showNews(news, news.getSluggedTitle());
	}
	
	@Get
	@CustomBrutauthRules({LoggedRule.class, InputRule.class})
	public void newsForm() {
		result.include("newsActive", true);
		result.include("noDefaultActive", true);
	}
	
	@Get
	@CustomBrutauthRules(EditNewsRule.class)
	public void newsEditForm(@Load News news) {
		result.include("news", news);
		result.include("newsActive", true);
		result.include("noDefaultActive", true);
	}
	
	@Post
	@CustomBrutauthRules(EditNewsRule.class)
	public void saveEdit(@Load News news, String title, MarkedText description, String comment) {
		Information newInformation = new NewsInformation(title, description, currentUser, comment);
		news.enqueueChange(newInformation, UpdateStatus.NO_NEED_TO_APPROVE);
		result.redirectTo(this).showNews(news, news.getSluggedTitle());
	}
	
	@Get
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
	
	@Post
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
