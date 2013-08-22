package br.com.caelum.brutal.controllers;

import java.util.Arrays;

import javax.inject.Inject;

import br.com.caelum.brutal.brutauth.auth.rules.EditAnswerRule;
import br.com.caelum.brutal.brutauth.auth.rules.LoggedRule;
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
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.plugin.hibernate4.extra.Load;
import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Get;
import br.com.caelum.vraptor4.Post;
import br.com.caelum.vraptor4.Result;
import br.com.caelum.vraptor4.Validator;
import br.com.caelum.vraptor4.core.Localization;
import br.com.caelum.vraptor4.view.Results;

@Controller
public class AnswerController {
	@Inject private Result result;
	@Inject private AnswerDAO answers;
	@Inject private LoggedUser currentUser;
	@Inject private Localization localization;
    @Inject private KarmaCalculator calculator;
	@Inject private MessageFactory messageFactory;
	@Inject private Validator validator;
	@Inject private AnsweredByValidator answeredByValidator;
	@Inject private NotificationManager notificationManager;
	@Inject private WatcherDAO watchers;
	@Inject private ReputationEventDAO reputationEvents;
	
	@Get("/resposta/editar/{answer.id}")
	@CustomBrutauthRules(EditAnswerRule.class)
	public void answerEditForm(@Load Answer answer) {
		result.include("answer",  answer);
	}

	@Post("/resposta/editar/{original.id}")
	@CustomBrutauthRules(EditAnswerRule.class)
	public void edit(@Load Answer original, String description, String comment) {
		AnswerInformation information = new AnswerInformation(description, currentUser, comment);

		validator.validate(information);
		validator.onErrorRedirectTo(this).answerEditForm(original);
		
		UpdateStatus status = original.updateWith(information);
		answers.save(original);

		result.include("messages", Arrays.asList(messageFactory.build("confirmation", status.getMessage())));
		Question originalQuestion = original.getMainThread();
		result.redirectTo(QuestionController.class).showQuestion(originalQuestion, originalQuestion.getSluggedTitle());
	}
	
	@Post("/responder/{question.id}")
	@CustomBrutauthRules(LoggedRule.class)
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
				watchers.add(question, new Watcher(current));
			}
        } else {
        	result.include("answer", answer);
        	answeredByValidator.onErrorRedirectTo(QuestionController.class).showQuestion(question, question.getSluggedTitle());
        }
		
	}
	
	@Post("/marcar-como-solucao/{solutionId}")
	public void markAsSolution(Long solutionId) {
		Answer solution = answers.getById(solutionId);
		Question question = solution.getMainThread();
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
    	Question question = answer.getMainThread();
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
	    	solution.getMainThread().getAuthor().descreaseKarma(karmaForQuestionAuthorSolution);
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
	        solution.getMainThread().getAuthor().increaseKarma(karmaForQuestionAuthorSolution);
    	}
    }

    private ReputationEvent solvedQuestionEvent(Answer solution) {
    	return new ReputationEvent(EventType.SOLVED_QUESTION, 
    			solution.getMainThread(), solution.getAuthor());
    }
    
    private ReputationEvent markedSolutionEvent(Answer solution) {
    	return new ReputationEvent(EventType.MARKED_SOLUTION, 
    			solution.getMainThread(), solution.getMainThread().getAuthor());
    }

}
