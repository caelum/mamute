package br.com.caelum.brutal.controllers;

import java.util.Arrays;
import java.util.List;

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
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.providers.RequiresTransaction;
import br.com.caelum.brutal.validators.QuestionInformationValidator;
import br.com.caelum.brutal.validators.TagsValidator;
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
	private final LoggedUser currentUser;
	private final TagsValidator tagsValidator;
	private final MessageFactory messageFactory;
	private final QuestionInformationValidator informationValidator;
	private final AuthorizationSystem authorizationSystem;

	public QuestionController(Result result, QuestionDAO questionDAO, TagDAO tags, VoteDAO votes, LoggedUser currentUser,
			TagsValidator tagsValidator, MessageFactory messageFactory, QuestionInformationValidator questionInformationValidator,
			AuthorizationSystem authorizationSystem) {
		this.result = result;
		this.questions = questionDAO;
		this.tags = tags;
		this.votes = votes;
		this.currentUser = currentUser;
		this.tagsValidator = tagsValidator;
		this.messageFactory = messageFactory;
		this.informationValidator = questionInformationValidator;
		this.authorizationSystem = authorizationSystem;
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
		
		if (validate(loadedTags) && informationValidator.validate(information)) {
			UpdateStatus status = original.updateWith(information);
			questions.save(original);
			result.include("messages", Arrays.asList(messageFactory.build("confirmation",status.getMessage())));
			result.redirectTo(this).showQuestion(id, original.getSluggedTitle());
		}
		tagsValidator.onErrorRedirectTo(this).questionEditForm(id);
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
		result.include("questionTags", question.getInformation().getTags());
		result.include("question", question);
	}

	@Post("/question/ask")
	@LoggedAccess
	public void newQuestion(String title, String description, String tagNames) {
		List<Tag> loadedTags = tags.findAllByNames(tagNames);
		QuestionInformation information = new QuestionInformation(title, description, currentUser, loadedTags, "new");
		if(validate(loadedTags) && informationValidator.validate(information)){
			Question question = new Question(information, currentUser.getCurrent());
			questions.save(question);
			result.redirectTo(this).showQuestion(question.getId(), question.getSluggedTitle());
		}
		tagsValidator.onErrorRedirectTo(this).questionForm();
	}

	private boolean validate(List<Tag> tags) {
		for (Tag tag : tags) {
			if(!tagsValidator.validate(tag)){
				return false;
			}
		}
		return true;
	}
}
