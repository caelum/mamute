package br.com.caelum.brutal.controllers;

import static br.com.caelum.brutal.util.TagsSplitter.splitTags;
import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import br.com.caelum.brutal.auth.FacebookAuthService;
import br.com.caelum.brutal.brutauth.auth.rules.EditQuestionRule;
import br.com.caelum.brutal.brutauth.auth.rules.InputRule;
import br.com.caelum.brutal.brutauth.auth.rules.LoggedRule;
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
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.plugin.hibernate4.extra.Load;
import br.com.caelum.vraptor.serialization.xstream.XStreamBuilder;
import br.com.caelum.vraptor.view.Results;

import com.thoughtworks.xstream.XStream;

@Controller
public class QuestionController {

	private Result result;
	private QuestionDAO questions;
	private TagDAO tags;
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
	private XStream xml;
	private BrutalValidator brutalValidator;


	/**
	 * @deprecated CDI eyes only 
	 */
	public QuestionController() {
	}
	
	@Inject
	public QuestionController(Result result, QuestionDAO questionDAO, TagDAO tags, 
			VoteDAO votes, LoggedUser currentUser, FacebookAuthService facebook,
			TagsValidator tagsValidator, MessageFactory messageFactory,
			Validator validator, PostViewCounter viewCounter,
			Linker linker, WatcherDAO watchers, 
			ReputationEventDAO reputationEvents, 
			XStreamBuilder xstreamBuilder, BrutalValidator brutalValidator) {
		this.result = result;
		this.questions = questionDAO;
		this.tags = tags;
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
		this.xml = xstreamBuilder.withoutRoot().xmlInstance();
	}

	@Get("/perguntar")
	@IncludeAllTags
	@CustomBrutauthRules(LoggedRule.class)
	public void questionForm() {
		String allTags = xml.toXML(tags.all());
		result.include("allTags", allTags);
	}
	
	@Get("/pergunta/editar/{question.id}")
	@IncludeAllTags
	@CustomBrutauthRules(EditQuestionRule.class)
	public void questionEditForm(@Load Question question) {
		result.include("question",  question);
		String allTags = xml.toXML(tags.all());
		result.include("allTags", allTags);
	}

	@Post("/pergunta/editar/{original.id}")
	@CustomBrutauthRules(EditQuestionRule.class)
	public void edit(@Load Question original, String title, String description, String tagNames, 
			String comment) {

		List<String> splitedTags = splitTags(tagNames);
		List<Tag> loadedTags = tags.findAllDistinct(splitedTags);
		QuestionInformation information = new QuestionInformation(title, description, this.currentUser, loadedTags, comment);
		brutalValidator.validate(information);
		UpdateStatus status = original.updateWith(information);
		
		result.include("editComment", comment);
		result.include("question", original);
		validate(loadedTags, splitedTags);
		validator.onErrorUse(Results.page()).of(this.getClass()).questionEditForm(original);
		
		questions.save(original);
		result.include("messages",
				Arrays.asList(messageFactory.build("confirmation", status.getMessage())));
		result.redirectTo(this).showQuestion(original, original.getSluggedTitle());
	}
	
	@Get("/{question.id:[0-9]+}-{sluggedTitle}")
	public void showQuestion(@Load Question question, String sluggedTitle){
		User current = currentUser.getCurrent();
		if (question.isVisibleFor(current)){
			result.include("markAsSolution", question.canMarkAsSolution(current));
			result.include("showUpvoteBanner", !current.isVotingEnough());
			
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
	
	private void redirectToRightUrl(Question question, String sluggedTitle) {
		if (!question.getSluggedTitle().equals(sluggedTitle)) {
			result.redirectTo(this).showQuestion(question,
					question.getSluggedTitle());
			return;
		}
	}

	@Post("/perguntar")
	@CustomBrutauthRules({LoggedRule.class, InputRule.class})
	public void newQuestion(String title, 	String description, String tagNames, boolean watching) {
		List<String> splitedTags = splitTags(tagNames);
		List<Tag> foundTags = tags.findAllDistinct(splitedTags);
		QuestionInformation information = new QuestionInformation(title, description, currentUser, foundTags, "new question");
		brutalValidator.validate(information);
		User author = currentUser.getCurrent();
		Question question = new Question(information, author);
		
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
