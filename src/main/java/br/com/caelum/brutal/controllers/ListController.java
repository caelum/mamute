package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.page;

import java.util.List;

import org.joda.time.DateTime;

import br.com.caelum.brutal.components.RecentTagsContainer;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
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

	@Get("/")
	public void home() {
		result.include("questions", questions.all());
		result.include("recentTags", tags.getRecentTagsSince(new DateTime().minusMonths(3)));
	}
	
	@Get("/list/unsolved")
	public void unsolved() {
		result.include("questions", questions.unsolved());
		result.include("recentTags", tags.getRecentTagsSince(new DateTime().minusMonths(3)));
		result.use(page()).of(ListController.class).home();
	}
	
	@Get("/list/withTag/{tagName}")
	public void withTag(String tagName) {
		Tag tag = tags.findByName(tagName);
		List<Question> questionsWithTag = questions.withTag(tag);
		result.include("recentTags", tags.getRecentTagsSince(new DateTime().minusMonths(3)));
		result.include("questions", questionsWithTag);
		result.use(page()).of(ListController.class).home();
	}

}
