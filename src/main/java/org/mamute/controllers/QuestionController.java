package org.mamute.controllers;

import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.mamute.auth.FacebookAuthService;
import org.mamute.brutauth.auth.rules.EditQuestionRule;
import org.mamute.brutauth.auth.rules.InputRule;
import org.mamute.brutauth.auth.rules.LoggedRule;
import org.mamute.brutauth.auth.rules.ModeratorOnlyRule;
import org.mamute.dao.QuestionDAO;
import org.mamute.dao.ReputationEventDAO;
import org.mamute.dao.VoteDAO;
import org.mamute.dao.WatcherDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.interceptors.IncludeAllTags;
import org.mamute.managers.TagsManager;
import org.mamute.model.EventType;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;
import org.mamute.model.QuestionInformation;
import org.mamute.model.ReputationEvent;
import org.mamute.model.Tag;
import org.mamute.model.UpdateStatus;
import org.mamute.model.User;
import org.mamute.model.post.PostViewCounter;
import org.mamute.model.watch.Watcher;
import org.mamute.util.TagsSplitter;
import org.mamute.validators.TagsValidator;
import org.mamute.vraptor.Linker;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.hibernate.extra.Load;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;

@Routed
@Controller
public class QuestionController {

	private Result result;
	private QuestionDAO questions;
	private VoteDAO votes;
	private LoggedUser currentUser;
	private TagsValidator tagsValidator;
	private MessageFactory messageFactory;
	private Validator validator;
	private FacebookAuthService facebook;
	private PostViewCounter viewCounter;
	private Linker linker;
	private WatcherDAO watchers;
	private ReputationEventDAO reputationEvents;
	private BrutalValidator brutalValidator;
	private TagsManager tagsManager;
	private TagsSplitter splitter;


	/**
	 * @deprecated CDI eyes only 
	 */
	public QuestionController() {
	}
	
	@Inject
	public QuestionController(Result result, QuestionDAO questionDAO, 
			VoteDAO votes, LoggedUser currentUser, FacebookAuthService facebook,
			TagsValidator tagsValidator, MessageFactory messageFactory,
			Validator validator, PostViewCounter viewCounter,
			Linker linker, WatcherDAO watchers, ReputationEventDAO reputationEvents,
			BrutalValidator brutalValidator, TagsManager tagsManager, TagsSplitter splitter) {
		this.result = result;
		this.questions = questionDAO;
		this.votes = votes;
		this.currentUser = currentUser;
		this.facebook = facebook;
		this.tagsValidator = tagsValidator;
		this.messageFactory = messageFactory;
		this.validator = validator;
		this.viewCounter = viewCounter;
		this.linker = linker;
		this.watchers = watchers;
		this.reputationEvents = reputationEvents;
		this.brutalValidator = brutalValidator;
		this.tagsManager = tagsManager;
		this.splitter = splitter;
	}

	@Get
	@IncludeAllTags
	@CustomBrutauthRules(LoggedRule.class)
	public void questionForm() {
	}
	
	@Get
	@IncludeAllTags
	@CustomBrutauthRules(EditQuestionRule.class)
	public void questionEditForm(@Load Question question) {
		result.include("question",  question);
	}

	@Post
	@CustomBrutauthRules(EditQuestionRule.class)
	public void edit(@Load Question original, String title, String description, String tagNames, 
			String comment) {

		List<String> splitedTags = splitter.splitTags(tagNames);
		List<Tag> loadedTags = tagsManager.findOrCreate(splitedTags);
		validate(loadedTags, splitedTags);
		
		QuestionInformation information = new QuestionInformation(title, description, this.currentUser, loadedTags, comment);
		brutalValidator.validate(information);
		UpdateStatus status = original.updateWith(information);

		validator.onErrorUse(Results.page()).of(this.getClass()).questionEditForm(original);
		
		result.include("editComment", comment);
		result.include("question", original);
		
		questions.save(original);
		result.include("messages",
				Arrays.asList(messageFactory.build("confirmation", status.getMessage())));
		result.redirectTo(this).showQuestion(original, original.getSluggedTitle());
	}
	
	@Get
	public void showQuestion(@Load Question question, String sluggedTitle){
		User current = currentUser.getCurrent();
		if (question.isVisibleFor(current)){
			result.include("markAsSolution", question.canMarkAsSolution(current));
			result.include("showUpvoteBanner", !current.isVotingEnough());
			result.include("editedLink", true);
			
			redirectToRightUrl(question, sluggedTitle);
			viewCounter.ping(question);
			boolean isWatching = watchers.ping(question, current);
			result.include("currentVote", votes.previousVoteFor(question.getId(), current, Question.class));
			result.include("answers", votes.previousVotesForAnswers(question, current));
			result.include("commentsWithVotes", votes.previousVotesForComments(question, current));
			result.include("questionTags", question.getTags());
			result.include("recentQuestionTags", question.getTagsUsage());
			result.include("relatedQuestions", questions.getRelatedTo(question));
			result.include("question", question);
			result.include("isWatching", isWatching);
			result.include("userMediumPhoto", true);
			linker.linkTo(this).showQuestion(question, sluggedTitle);
			result.include("facebookUrl", facebook.getOauthUrl(linker.get()));
		} else {
			result.notFound();
		}
	}

	@Post
	@CustomBrutauthRules({LoggedRule.class, InputRule.class})
	public void newQuestion(String title, String description, String tagNames, boolean watching) {
		List<String> splitedTags = splitter.splitTags(tagNames);

		List<Tag> foundTags = tagsManager.findOrCreate(splitedTags);
		validate(foundTags, splitedTags);
		
		QuestionInformation information = new QuestionInformation(title, description, currentUser, foundTags, "new question");
		brutalValidator.validate(information);
		User author = currentUser.getCurrent();
		Question question = new Question(information, author);
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

	@CustomBrutauthRules(ModeratorOnlyRule.class)
	@Get
	public void showVoteInformation (@Load Question question, String sluggedTitle){
		result.include("question", question);
		redirectToRightUrl(question, sluggedTitle);
	}
	
	private boolean validate(List<Tag> foundTags, List<String> splitedTags) {
		return tagsValidator.validate(foundTags, splitedTags);
	}
	
	private void redirectToRightUrl(Question question, String sluggedTitle) {
		if (!question.getSluggedTitle().equals(sluggedTitle)) {
			result.redirectTo(this).showQuestion(question,
					question.getSluggedTitle());
			return;
		}
	}
}
