package br.com.caelum.brutal.controllers;

import java.util.Arrays;
import java.util.List;

import br.com.caelum.brutal.auth.FacebookAuthService;
import br.com.caelum.brutal.auth.LoggedAccess;
import br.com.caelum.brutal.auth.rules.AuthorizationSystem;
import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.QuestionViewCounter;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.reputation.rules.ReputationEvent;
import br.com.caelum.brutal.reputation.rules.ReputationEvents;
import br.com.caelum.brutal.validators.TagsValidator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;

@Resource
public class QuestionController {

	private final Result result;
	private final QuestionDAO questions;
	private final TagDAO tags;
	private final VoteDAO votes;
	private final LoggedUser currentUser;
	private final TagsValidator tagsValidator;
	private final MessageFactory messageFactory;
	private final AuthorizationSystem authorizationSystem;
	private final Validator validator;
	private final FacebookAuthService facebook;
	private final QuestionViewCounter viewCounter;

	public QuestionController(Result result, QuestionDAO questionDAO, TagDAO tags, 
			VoteDAO votes, LoggedUser currentUser, FacebookAuthService facebook,
			TagsValidator tagsValidator, MessageFactory messageFactory,
			AuthorizationSystem authorizationSystem, Validator validator, QuestionViewCounter viewCounter) {
		this.result = result;
		this.questions = questionDAO;
		this.tags = tags;
		this.votes = votes;
		this.currentUser = currentUser;
		this.facebook = facebook;
		this.tagsValidator = tagsValidator;
		this.messageFactory = messageFactory;
		this.authorizationSystem = authorizationSystem;
		this.validator = validator;
		this.viewCounter = viewCounter;
	}

	@Get("/question/ask")
	@LoggedAccess
	public void questionForm() {
	}

	@Get("/question/edit/{questionId}")
	public void questionEditForm(Long questionId) {
		Question question = questions.getById(questionId);
		authorizationSystem.canEdit(question, PermissionRulesConstants.EDIT_QUESTION);
		
		result.include("question",  questions.getById(questionId));
	}

	@Post("/question/edit/{id}")
	public void edit(String title, String description, String tagNames, Long id, String comment) {
		Question original = questions.getById(id);
		authorizationSystem.canEdit(original, PermissionRulesConstants.EDIT_QUESTION);
		
		List<Tag> loadedTags = tags.findAllByNames(tagNames);
		QuestionInformation information = new QuestionInformation(title, description, this.currentUser, loadedTags, comment);
		
		validator.validate(information);
		validate(loadedTags);
		validator.onErrorRedirectTo(this).questionEditForm(id);
		
		UpdateStatus status = original.updateWith(information);
		questions.save(original);
		result.include("messages",
				Arrays.asList(messageFactory.build("confirmation", status.getMessage())));
		result.redirectTo(this).showQuestion(id, original.getSluggedTitle());
	}
	
	@Get("/questions/{questionId}/{sluggedTitle}")
	public void showQuestion(Long questionId, String sluggedTitle) {
		Question question = questions.getById(questionId);
		if (!question.getSluggedTitle().equals(sluggedTitle)) {
			result.redirectTo(this).showQuestion(question.getId(),
					question.getSluggedTitle());
			return;
		}
		viewCounter.ping(question);
		User author = currentUser.getCurrent();
		result.include("currentVote", votes.previousVoteFor(questionId, author, Question.class));
		result.include("answers", votes.previousVotesForAnswers(question, author));
		result.include("questionTags", question.getInformation().getTags());
		result.include("question", question);
		result.include("facebookUrl", facebook.getOauthUrl());
	}

	@Post("/question/ask")
	@LoggedAccess
	@ReputationEvent(ReputationEvents.NEW_QUESTION)
	public void newQuestion(String title, String description, String tagNames) {
		List<Tag> loadedTags = tags.findAllByNames(tagNames);
		QuestionInformation information = new QuestionInformation(title, description, currentUser, loadedTags, "new question");
		
		validator.validate(information);
		validate(loadedTags);
		validator.onErrorRedirectTo(this).questionForm();
	
		Question question = new Question(information, currentUser.getCurrent());
		questions.save(question);
		result.redirectTo(this).showQuestion(question.getId(), question.getSluggedTitle());
	}

	private boolean validate(List<Tag> tags) {
		for (Tag tag : tags) {
			if (!tagsValidator.validate(tag)) {
				return false;
			}
		}
		return true;
	}
}
