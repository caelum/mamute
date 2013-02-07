package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.page;

import java.util.List;

import org.joda.time.DateTime;

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

	public ListController(QuestionDAO questions, TagDAO tags, Result result) {
		this.questions = questions;
		this.tags = tags;
		this.result = result;
	}

	@Get("/")
	public void home() {
		result.include("questions", questions.all());
		result.include("tagsUsage", tags.getRecentTagsUsageSince(new DateTime().minusMonths(3)));
	}
	
	@Get("/list/unanswered")
	public void unanswered() {
		result.include("questions", questions.unanswered());
		result.use(page()).of(ListController.class).home();
	}
	
	@Get("/list/withTag/{tagName}")
	public void withTag(String tagName) {
		Tag tag = tags.findByName(tagName);
		List<Question> questionsWithTag = questions.withTag(tag);
		result.include("questions", questionsWithTag);
		result.use(page()).of(ListController.class).home();
	}

}
