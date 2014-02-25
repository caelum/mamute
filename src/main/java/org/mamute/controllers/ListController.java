package org.mamute.controllers;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.mamute.components.RecentTagsContainer;
import org.mamute.dao.NewsDAO;
import org.mamute.dao.QuestionDAO;
import org.mamute.dao.TagDAO;
import org.mamute.model.LoggedUser;
import org.mamute.model.News;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.mamute.providers.BrutalRoutesParser;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;

@Controller
public class ListController {

	@Inject private LoggedUser loggedUser;
	@Inject private QuestionDAO questions;
	@Inject private Result result;
	@Inject private TagDAO tags;
	@Inject private RecentTagsContainer recentTagsContainer;
	@Inject private NewsDAO newses;
	
	/**
	 * actually, this path will not be used, we use the path defined in the current environment
	 * be careful when modifying its signature
	 * @see BrutalRoutesParser
	 */
	@Get("/")
	public void home(Integer p) {
		Integer page = getPage(p);
		List<Question> visible = questions.allVisible(page);
		if (visible.isEmpty() && page != 1) {
			result.notFound();
			return;
		}
		List<String> tabs = Arrays.asList("voted", "answered", "viewed");
		result.include("tabs", tabs);

		result.include("questions", visible);
		result.include("totalPages", questions.numberOfPages());
		result.include("currentPage", page);
		result.include("currentUser", loggedUser);
	}

	@Get("/top")
	public void topRaw() {
		result.redirectTo(this).top("voted");
	}

	@Get("/top/{section}")
	public void top(String section) {
		Integer count = 35;
		
		List<String> tabs = Arrays.asList("voted", "answered", "viewed");
		if (!tabs.contains(section)) {
			section = tabs.get(0);
			result.redirectTo(this).top(section);
			return;
		}

		DateTime since = DateTime.now().minusMonths(2);
		List<Question> top = questions.top(section, count, since);
		
		if (top.isEmpty()) {
			result.notFound();
			return;
		}
		result.include("tabs", tabs);
		result.include("section", section);
		result.include("questions", top);
		result.include("currentUser", loggedUser);
	}
	
	@Get("/perguntas")
	public void hackedIndex() {
		result.redirectTo(this).home(1);
	}

	@Get({"/noticias", "/noticias/"})
	public void news(Integer p) {
		Integer page = getPage(p);
		List<News> visible = newses.allVisible(page, 25);
		if (visible.isEmpty() && page != 1) {
			result.notFound();
			return;
		}
		result.include("newses", visible);
		result.include("totalPages", newses.numberOfPages(25));
		result.include("currentPage", page);
		result.include("newsActive", true);
		result.include("noDefaultActive", true);
	}
	
	@Get("/nao-resolvido")
	public void unsolved(Integer p) {
		Integer page = getPage(p);
		result.include("questions", questions.unsolvedVisible(page));
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
		result.include("currentPage", page);
		result.include("totalPages", questions.totalPagesUnsolvedVisible());
	}
	
	@Get("/sem-respostas")
	public void unanswered(Integer p) {
		Integer page = getPage(p);
		result.include("questions", questions.unanswered(page));
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
		result.include("currentPage", page);
		result.include("totalPages", questions.totalPagesWithoutAnswers());
		result.include("unansweredActive", true);
		result.include("noDefaultActive", true);
	}
	
	@Get("/tag/{tagName}")
	public void withTag(String tagName, Integer p, boolean semRespostas) {
		Integer page = getPage(p);
		Tag tag = tags.findByName(tagName);
		if(tag == null){
			result.notFound();
			return;
		}
		List<Question> questionsWithTag = questions.withTagVisible(tag, page, semRespostas);
		result.include("totalPages", questions.numberOfPages(tag));
		result.include("tag", tag);
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
		result.include("questions", questionsWithTag);
		result.include("currentPage", page);
		result.include("hasAbout", tags.hasAbout(tag));
		if (semRespostas) {
			result.include("unansweredActive", true);
			result.include("noDefaultActive", true);
			result.include("unansweredTagLinks", true);
		}
	}
	
	@Get("/tags")
	public void listTags(){
		result.include("tags", tags.all());
	}
	
	@Get("/mediakit")
	public void mediakit(){
		result.forwardTo("/WEB-INF/jsp/list/mediakit.jspf");
	}

	private Integer getPage(Integer p) {
		Integer page = p == null ? 1 : p;
		return page;
	}

}
