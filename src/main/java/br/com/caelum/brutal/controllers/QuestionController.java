package br.com.caelum.brutal.controllers;

import static br.com.caelum.brutal.util.TagsSplitter.splitTags;

import java.util.Arrays;
import java.util.List;

import br.com.caelum.brutal.auth.FacebookAuthService;
import br.com.caelum.brutal.auth.LoggedAccess;
import br.com.caelum.brutal.auth.rules.AuthorizationSystem;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.QuestionViewCounter;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.brutal.reputation.rules.ReputationEvent;
import br.com.caelum.brutal.reputation.rules.ReputationEvents;
import br.com.caelum.brutal.validators.TagsValidator;
import br.com.caelum.brutal.vraptor.Linker;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.util.hibernate.extra.Load;
import br.com.caelum.vraptor.view.Results;

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
	private Linker linker;
	private final WatcherDAO watchers;

	public QuestionController(Result result, QuestionDAO questionDAO, TagDAO tags, 
			VoteDAO votes, LoggedUser currentUser, FacebookAuthService facebook,
			TagsValidator tagsValidator, MessageFactory messageFactory,
			AuthorizationSystem authorizationSystem, Validator validator, 
			QuestionViewCounter viewCounter, Linker linker, WatcherDAO watchers) {
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
		this.linker = linker;
		this.watchers = watchers;
	}

	@Get("/perguntar")
	@LoggedAccess
	public void questionForm() {
	}
	
	@Get("/pergunta/editar/{questionId}")
	public void questionEditForm(Long questionId) {
		Question question = questions.getById(questionId);
		authorizationSystem.canEdit(question, authorizationSystem.ruleForQuestionEdit());
		
		result.include("question",  questions.getById(questionId));
	}

	@Post("/pergunta/editar/{id}")
	public void edit(Long id, String title, String description, String tagNames, String comment) {
		Question original = questions.getById(id);
		authorizationSystem.canEdit(original, authorizationSystem.ruleForQuestionEdit());
		
		List<String> splitedTags = splitTags(tagNames);
		List<Tag> loadedTags = tags.findAllWithoutRepeat(splitedTags);
		QuestionInformation information = new QuestionInformation(title, description, this.currentUser, loadedTags, comment);
		UpdateStatus status = original.updateWith(information);
		
		result.include("editComment", comment);
		result.include("question", original);
		validator.validate(information);
		validate(loadedTags, splitedTags);
		validator.onErrorUse(Results.page()).of(this.getClass()).questionEditForm(id);
		
		questions.save(original);
		result.include("messages",
				Arrays.asList(messageFactory.build("confirmation", status.getMessage())));
		result.redirectTo(this).showQuestion(original, original.getSluggedTitle());
	}
	
	@Get("/{question.id:[0-9]+}-{sluggedTitle}")
	public void showQuestion(@Load Question question, String sluggedTitle){
		redirectToRightUrl(question, sluggedTitle);
		User current = currentUser.getCurrent();
		if (question.isVisibleFor(current)){
			viewCounter.ping(question);
			boolean isWatching = watchers.ping(question, current);
			result.include("currentVote", votes.previousVoteFor(question.getId(), current, Question.class));
			result.include("answers", votes.previousVotesForAnswers(question, current));
			result.include("commentsWithVotes", votes.previousVotesForComments(question, current));
			result.include("questionTags", question.getInformation().getTags());
			result.include("question", question);
			result.include("isWatching", isWatching);
			linker.linkTo(this).showQuestion(question, sluggedTitle);
			result.include("facebookUrl", facebook.getOauthUrl(linker.get()));
		}else{
			result.notFound();
		}
	}
	
	private void redirectToRightUrl(Question question, String sluggedTitle) {
		if (!question.getSluggedTitle().equals(sluggedTitle)) {
			result.redirectTo(this).showQuestion(question,
					question.getSluggedTitle());
			return;
		}
	}

	@Post("/perguntar")
	@LoggedAccess
	@ReputationEvent(ReputationEvents.NEW_QUESTION)
	public void newQuestion(String title, String description, String tagNames) {
		List<String> splitedTags = splitTags(tagNames);
		List<Tag> foundTags = tags.findAllWithoutRepeat(splitedTags);
		QuestionInformation information = new QuestionInformation(title, description, currentUser, foundTags, "new question");
		User author = currentUser.getCurrent();
		Question question = new Question(information, author);
		
		validator.validate(information);
		validate(foundTags, splitedTags);
		result.include("question", question);
		validator.onErrorRedirectTo(this).questionForm();
		
		questions.save(question);
		watchers.add(new Watcher(author, question));
		result.redirectTo(this).showQuestion(question, question.getSluggedTitle());

	}
	
	@Post("/watch/{questionId}")
	@LoggedAccess
	public void watch(Long questionId) {
		Question question = questions.getById(questionId);
		User user = currentUser.getCurrent();
		Watcher watcher = new Watcher(user, question);
		watchers.addOrRemove(watcher);
		result.nothing();
	}

	

	private boolean validate(List<Tag> foundTags, List<String> splitedTags) {
		return tagsValidator.validate(foundTags, splitedTags);
	}
}
