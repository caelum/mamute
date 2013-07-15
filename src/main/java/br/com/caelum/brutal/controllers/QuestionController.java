package br.com.caelum.brutal.controllers;

import static br.com.caelum.brutal.util.TagsSplitter.splitTags;
import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.List;

import br.com.caelum.brutal.auth.FacebookAuthService;
import br.com.caelum.brutal.auth.LoggedAccess;
import br.com.caelum.brutal.auth.rules.AuthorizationSystem;
import br.com.caelum.brutal.auth.rules.Rules;
import br.com.caelum.brutal.brutauth.auth.rules.BrutauthAuthorRule;
import br.com.caelum.brutal.brutauth.auth.rules.BrutauthModeratorRule;
import br.com.caelum.brutal.brutauth.rules.AuthRules;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.ReputationEventDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.interceptors.IncludeAllTags;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.PostViewCounter;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.watch.Watcher;
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
	private final PostViewCounter viewCounter;
	private Linker linker;
	private final WatcherDAO watchers;
	private final ReputationEventDAO reputationEvents;

	public QuestionController(Result result, QuestionDAO questionDAO, TagDAO tags, 
			VoteDAO votes, LoggedUser currentUser, FacebookAuthService facebook,
			TagsValidator tagsValidator, MessageFactory messageFactory,
			AuthorizationSystem authorizationSystem, Validator validator, 
			PostViewCounter viewCounter, Linker linker, WatcherDAO watchers, 
			ReputationEventDAO reputationEvents) {
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
		this.reputationEvents = reputationEvents;
	}

	@Get("/perguntar")
	@LoggedAccess
	@IncludeAllTags
	public void questionForm() {
	}
	
	@Get("/pergunta/editar/{questionId}")
	@IncludeAllTags
	public void questionEditForm(Long questionId) {
		Question question = questions.getById(questionId);
		authorizationSystem.authorize(question, Rules.EDIT_QUESTION);
		
		result.include("question",  questions.getById(questionId));
	}

	@Post("/pergunta/editar/{id}")
	public void edit(Long id, String title, String description, String tagNames, String comment) {
		Question original = questions.getById(id);
		authorizationSystem.authorize(original, Rules.EDIT_QUESTION);
		
		List<String> splitedTags = splitTags(tagNames);
		List<Tag> loadedTags = tags.findAllDistinct(splitedTags);
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
	@AuthRules({BrutauthAuthorRule.class, BrutauthModeratorRule.class})
	public void showQuestion(@Load Question question, String sluggedTitle){
		User current = currentUser.getCurrent();
		if (question.isVisibleFor(current)){
			redirectToRightUrl(question, sluggedTitle);
			viewCounter.ping(question);
			boolean isWatching = watchers.ping(question, current);
			result.include("currentVote", votes.previousVoteFor(question.getId(), current, Question.class));
			result.include("answers", votes.previousVotesForAnswers(question, current));
			result.include("commentsWithVotes", votes.previousVotesForComments(question, current));
			result.include("questionTags", question.getInformation().getTags());
			result.include("question", question);
			result.include("isWatching", isWatching);
			result.include("userMediumPhoto", true);
			linker.linkTo(this).showQuestion(question, sluggedTitle);
			result.include("facebookUrl", facebook.getOauthUrl(linker.get()));
		} else {
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
	public void newQuestion(String title, String description, String tagNames, boolean watching) {
		List<String> splitedTags = splitTags(tagNames);
		List<Tag> foundTags = tags.findAllDistinct(splitedTags);
		QuestionInformation information = new QuestionInformation(title, description, currentUser, foundTags, "new question");
		User author = currentUser.getCurrent();
		Question question = new Question(information, author);
		
		validator.validate(information);
		validate(foundTags, splitedTags);
		result.include("question", question);
		validator.onErrorRedirectTo(this).questionForm();
		
		questions.save(question);
		ReputationEvent reputationEvent = new ReputationEvent(EventType.CREATED_QUESTION, question, author);
		author.increaseKarma(reputationEvent.getKarmaReward());
		reputationEvents.save(reputationEvent);
		if (watching) {
			watchers.add(question, new Watcher(author));
		}
		result.include("messages", asList(messageFactory.build("alert", "question.quality_reminder")));
		result.redirectTo(this).showQuestion(question, question.getSluggedTitle());

	}

	private boolean validate(List<Tag> foundTags, List<String> splitedTags) {
		return tagsValidator.validate(foundTags, splitedTags);
	}
}
