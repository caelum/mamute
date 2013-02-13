package br.com.caelum.brutal.controllers;

import java.util.Arrays;
import java.util.List;

import br.com.caelum.brutal.auth.LoggedAccess;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.model.CurrentUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.providers.RequiresTransaction;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class QuestionController {

	private final Result result;
	private final QuestionDAO questions;
	private final TagDAO tags;
	private final VoteDAO votes;
	private final CurrentUser currentUser;

	public QuestionController(Result result, QuestionDAO questionDAO, TagDAO tagDAO, VoteDAO votes, CurrentUser currentUser) {
		this.result = result;
		this.questions = questionDAO;
		this.tags = tagDAO;
		this.votes = votes;
		this.currentUser = currentUser;
	}

	@Get("/question/ask")
	@LoggedAccess
	public void questionForm() {
	}

	@Get("/question/edit/{questionId}")
	public void questionEditForm(Long questionId) {
		result.include("question",  questions.getById(questionId));
	}

	@Post("/question/edit/{id}")
	public void edit(String title, String description, String tagNames, Long id, String comment) {
		List<Tag> tags = this.tags.loadAll(tagNames, currentUser.getCurrent());
		QuestionInformation information = new QuestionInformation(title, description, this.currentUser, tags, comment);

		Question original = questions.getById(id);
		UpdateStatus status = original.updateWith(information);
		questions.save(original);
		result.include("confirmations", Arrays.asList(status));
		result.redirectTo(this).showQuestion(id, original.getSluggedTitle());
	}
	
	@Get("/questions/{questionId}/{sluggedTitle}")
	@RequiresTransaction
	public void showQuestion(Long questionId, String sluggedTitle) {
		Question question = questions.getById(questionId);
		if (!question.getSluggedTitle().equals(sluggedTitle)) {
			result.redirectTo(this).showQuestion(question.getId(),
					question.getSluggedTitle());
			return;
		}
		question.ping();
		User author = currentUser.getCurrent();
		result.include("currentVote", votes.previousVoteFor(questionId, author, Question.class));
		result.include("answers", votes.previousVotesForAnswers(question, author));
		result.include("questionTagsUsages", tags.findTagsUsageOf(question));
		result.include("question", question);
	}

	@Post("/question/ask")
	@LoggedAccess
	public void newQuestion(String title, String description, String tagNames) {
		List<Tag> tags = this.tags.loadAll(tagNames, currentUser.getCurrent());
		QuestionInformation information = new QuestionInformation(title, description, currentUser, tags, "new");
		Question question = new Question(information, currentUser.getCurrent());

		questions.save(question);
		result.redirectTo(this).showQuestion(question.getId(),
				question.getSluggedTitle());
	}
}
