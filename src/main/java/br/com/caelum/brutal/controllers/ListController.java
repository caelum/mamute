package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.page;

import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;

import br.com.caelum.brutal.components.RecentTagsContainer;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
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
	 * @see BrutalRoutesParser
	 */
	@Get("/")
	public void home() {
		result.include("questions", questions.allVisible());
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
	}
	
	@Get("/nao-resolvido")
	public void unsolved() {
		result.include("questions", questions.unsolvedVisible());
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
		result.use(page()).of(ListController.class).home();
	}
	
	@Get("/tag/{tagName}")
	public void withTag(String tagName) {
		Tag tag = tags.findByName(tagName);
		List<Question> questionsWithTag = questions.withTagVisible(tag);
		result.include("recentTags", tags.getRecentTagsSince(new DateTime().minusMonths(3)));
		result.include("questions", questionsWithTag);
		result.use(page()).of(ListController.class).home();
	}
	
	@Get("/tags")
	public void listTags(){
		result.include("tags", tags.all());
	}
	
	@Get("/erro")
	public void error(UserDAO users) {
		users.save(new User("chico sokol lalalala", "chico.sokol"+new Random().nextLong()+"@caelum.com.br"));
		result.redirectTo(this).home();
		throw new RuntimeException();
	}

}
