package br.com.caelum.brutal.controllers;

import java.util.List;

import br.com.caelum.brutal.components.RecentTagsContainer;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.providers.BrutalRoutesParser;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ListController {

	private final QuestionDAO questions;
	private final Result result;
	private final TagDAO tags;
	private final RecentTagsContainer recentTagsContainer;

	public ListController(QuestionDAO questions, TagDAO tags, Result result, RecentTagsContainer recentTagsContainer) {
		this.questions = questions;
		this.tags = tags;
		this.result = result;
		this.recentTagsContainer = recentTagsContainer;
	}

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
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
		result.include("totalPages", questions.numberOfPages());
		result.include("currentPage", page);
	}
	
	@Get("/nao-resolvido")
	public void unsolved() {
		result.include("questions", questions.unsolvedVisible());
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
	}
	
	@Get("/sem-respostas")
	public void unanswered() {
		result.include("questions", questions.noAnswers());
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
	}
	
	@Get("/tag/{tagName}")
	public void withTag(String tagName, Integer p) {
		Integer page = getPage(p);
		Tag tag = tags.findByName(tagName);
		List<Question> questionsWithTag = questions.withTagVisible(tag, page);
		result.include("totalPages", questions.numberOfPages(tag));
		result.include("tagName", tagName);
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
		result.include("questions", questionsWithTag);
		result.include("currentPage", page);
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
