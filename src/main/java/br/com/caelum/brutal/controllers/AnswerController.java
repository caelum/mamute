package br.com.caelum.brutal.controllers;

import java.util.Arrays;

import br.com.caelum.brutal.auth.LoggedAccess;
import br.com.caelum.brutal.auth.rules.AuthorizationSystem;
import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.ReputationEventDAO;
import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.mail.action.EmailAction;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.brutal.notification.NotificationManager;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;
import br.com.caelum.brutal.validators.AnsweredByValidator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.util.hibernate.extra.Load;
import br.com.caelum.vraptor.view.Results;

@Resource
public class AnswerController {
	private final Result result;
	private final AnswerDAO answers;
	private final LoggedUser currentUser;
	private final Localization localization;
    private final KarmaCalculator calculator;
	private final MessageFactory messageFactory;
	private final AuthorizationSystem authorizationSystem;
	private final Validator validator;
	private final AnsweredByValidator answeredByValidator;
	private final NotificationManager notificationManager;
	private final WatcherDAO watchers;
	private final ReputationEventDAO reputationEvents;

	public AnswerController(Result result, AnswerDAO dao, 
			LoggedUser user, Localization localization,
	        KarmaCalculator calculator, MessageFactory messageFactory, 
	        AuthorizationSystem authorizationSystem, Validator validator,
	        AnsweredByValidator answeredByValidator, NotificationManager notificationManager,
	        WatcherDAO watchers, ReputationEventDAO reputationEvents) {
		this.result = result;
		this.answers = dao;
		this.currentUser = user;
		this.localization = localization;
        this.calculator = calculator;
		this.messageFactory = messageFactory;
		this.authorizationSystem = authorizationSystem;
		this.validator = validator;
		this.answeredByValidator = answeredByValidator;
		this.notificationManager = notificationManager;
		this.watchers = watchers;
		this.reputationEvents = reputationEvents;
	}


	@Get("/resposta/editar/{id}")
	public void answerEditForm(Long id) {
		Answer answer = answers.getById(id);
		authorizationSystem.canEdit(answer, authorizationSystem.ruleForAnswerEdit());
		
		result.include("answer",  answers.getById(id));
	}

	@Post("/resposta/editar/{id}")
	public void edit(Long id, String description, String comment) {
		Answer original = answers.getById(id);
		authorizationSystem.canEdit(original, authorizationSystem.ruleForAnswerEdit());
		
		AnswerInformation information = new AnswerInformation(description, currentUser, comment);

		validator.validate(information);
		validator.onErrorRedirectTo(this).answerEditForm(id);
		
		UpdateStatus status = original.updateWith(information);
		answers.save(original);

		result.include("messages", Arrays.asList(messageFactory.build("confirmation", status.getMessage())));
		Question originalQuestion = original.getQuestion();
		result.redirectTo(QuestionController.class).showQuestion(originalQuestion, originalQuestion.getSluggedTitle());
	}
	
	@Post("/responder/{question.id}")
	@LoggedAccess
	public void newAnswer(@Load Question question, String description, boolean watching) {
		User current = currentUser.getCurrent();
		boolean canAnswer = answeredByValidator.validate(question);
		AnswerInformation information = new AnswerInformation(description, currentUser, "new answer");
		Answer answer  = new Answer(information, question, current);
		if (canAnswer) {
    		question.touchedBy(current);
        	answers.save(answer);
        	ReputationEvent reputationEvent = new ReputationEvent(EventType.CREATED_ANSWER, question, current);
        	reputationEvents.save(reputationEvent);
    		current.increaseKarma(reputationEvent.getKarmaReward());
        	result.redirectTo(QuestionController.class).showQuestion(question, question.getSluggedTitle());
			notificationManager.sendEmailsAndInactivate(new EmailAction(answer, question));
			if (watching) {
				watchers.add(new Watcher(current, question));
			}
        } else {
        	result.include("answer", answer);
        	answeredByValidator.onErrorRedirectTo(QuestionController.class).showQuestion(question, question.getSluggedTitle());
        }
		
	}
	
	@Post("/marcar-como-solucao/{solutionId}")
	public void markAsSolution(Long solutionId) {
		Answer solution = answers.getById(solutionId);
		Question question = solution.getQuestion();
        if (!currentUser.getCurrent().isAuthorOf(question)) {
			result.use(Results.status()).forbidden(localization.getMessage("answer.error.not_autor"));
			result.redirectTo(QuestionController.class).showQuestion(question,
					question.getSluggedTitle());
			return;
		} 
        markOrRemoveSolution(solution);
        result.nothing();
	}

    private void markOrRemoveSolution(Answer answer) {
    	Question question = answer.getQuestion();
    	if (answer.isSolution()) {
    		decreaseKarmaOfOldSolution(answer);
    		answer.uncheckAsSolution();
    		return;
    	} 
		if (question.isSolved()) {
			decreaseKarmaOfOldSolution(question.getSolution());
		}
		answer.markAsSolution();
		increaseKarmaOfUsersInvolved(answer);
	}

    
    private void decreaseKarmaOfOldSolution(Answer solution) {
    	if (!solution.isTheSameAuthorOfQuestion()) {
	    	ReputationEvent solvedQuestion = solvedQuestionEvent(solution);
	    	int karmaForSolutionAuthor = calculator.karmaFor(solvedQuestion);
	    	
	    	ReputationEvent markedSolution = markedSolutionEvent(solution);
	    	int karmaForQuestionAuthorSolution = calculator.karmaFor(markedSolution);
	    	
	    	reputationEvents.delete(solvedQuestion);
	    	reputationEvents.delete(markedSolution);
	    	
	    	solution.getAuthor().descreaseKarma(karmaForSolutionAuthor);
	    	solution.getQuestion().getAuthor().descreaseKarma(karmaForQuestionAuthorSolution);
    	}
    }

    private void increaseKarmaOfUsersInvolved(Answer solution) {
    	if (!solution.isTheSameAuthorOfQuestion()) {
	    	ReputationEvent solvedQuestion = solvedQuestionEvent(solution);
	        int karmaForSolutionAuthor = calculator.karmaFor(solvedQuestion);
	        
	        ReputationEvent markedSolution = markedSolutionEvent(solution);
	        int karmaForQuestionAuthorSolution = calculator.karmaFor(markedSolution);
	        
	        reputationEvents.save(solvedQuestion);
	        reputationEvents.save(markedSolution);
	        solution.getAuthor().increaseKarma(karmaForSolutionAuthor);
	        solution.getQuestion().getAuthor().increaseKarma(karmaForQuestionAuthorSolution);
    	}
    }

    private ReputationEvent solvedQuestionEvent(Answer solution) {
    	return new ReputationEvent(EventType.SOLVED_QUESTION, 
    			solution.getQuestion(), solution.getAuthor());
    }
    
    private ReputationEvent markedSolutionEvent(Answer solution) {
    	return new ReputationEvent(EventType.MARKED_SOLUTION, 
    			solution.getQuestion(), solution.getAuthor());
    }

}
