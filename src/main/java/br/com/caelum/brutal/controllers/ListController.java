package br.com.caelum.brutal.controllers;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOnlyRule;
import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOrKarmaRule;
import br.com.caelum.brutal.components.RecentTagsContainer;
import br.com.caelum.brutal.dao.NewsDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.providers.BrutalRoutesParser;
import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Get;
import br.com.caelum.vraptor4.Result;

@Controller
public class ListController {

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
		result.include("questions", visible);
		result.include("totalPages", questions.numberOfPages());
		result.include("currentPage", page);
	}
	
	@Get("/perguntas")
	public void hackedIndex() {
		result.redirectTo(this).home(1);
	}

	@Get({"/noticias", "/noticias/"})
	public void news(Integer p) {
		Integer page = getPage(p);
		List<News> visible = newses.allVisible(page, 50);
		if (visible.isEmpty() && page != 1) {
			result.notFound();
			return;
		}
		result.include("newses", visible);
		result.include("totalPages", newses.numberOfPages(50));
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
		if (semRespostas) {
			result.include("unansweredActive", true);
			result.include("noDefaultActive", true);			
		}
		if(tag != null){
			List<Question> questionsWithTag = questions.withTagVisible(tag, page, semRespostas);
			result.include("totalPages", questions.numberOfPages(tag));
			result.include("tag", tag);
			result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
			result.include("questions", questionsWithTag);
			result.include("currentPage", page);
			result.include("hasAbout", tags.hasAbout(tag));
		}else{
			result.notFound();
		}
	}
	
	@Get("/tags")
	public void listTags(){
		result.include("tags", tags.all());
	}

	private Integer getPage(Integer p) {
		Integer page = p == null ? 1 : p;
		return page;
	}

}
