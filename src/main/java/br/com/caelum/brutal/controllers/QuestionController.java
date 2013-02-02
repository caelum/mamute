package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.Logged;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
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
	private final User currentUser;
	private final TagDAO tags;
	private final VoteDAO votes;

	public QuestionController(Result result, QuestionDAO questionDAO, TagDAO tagDAO, User currentUser, VoteDAO votes) {
		this.result = result;
		this.questions = questionDAO;
		this.tags = tagDAO;
		this.currentUser = currentUser;
		this.votes = votes;
	}

	@Get("/question/ask")
	@Logged
	public void questionForm() {
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
		result.include("currentVote", votes.previousVoteFor(questionId, currentUser, Question.class));
		result.include("question", question);
	}

	@Post("/question/ask")
	@Logged
	public void newQuestion(Question question, String tagNames) {
		question.setAuthor(currentUser);
		questions.save(question);
		for (String tagName : tagNames.split(" ")) {
			Tag newTag = tags.saveOrLoad(new Tag(tagName, "", currentUser));
			question.addTag(newTag);
		}
		result.redirectTo(this).showQuestion(question.getId(),
				question.getSluggedTitle());
	}
}
