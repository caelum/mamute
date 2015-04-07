package org.mamute.controllers;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.mamute.brutauth.auth.rules.EditAnswerRule;
import org.mamute.brutauth.auth.rules.InactiveQuestionRequiresMoreKarmaRule;
import org.mamute.brutauth.auth.rules.InputRule;
import org.mamute.brutauth.auth.rules.LoggedRule;
import org.mamute.dao.AnswerDAO;
import org.mamute.dao.AttachmentDao;
import org.mamute.dao.ReputationEventDAO;
import org.mamute.dao.WatcherDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.mail.action.EmailAction;
import org.mamute.model.*;
import org.mamute.model.watch.Watcher;
import org.mamute.notification.NotificationManager;
import org.mamute.reputation.rules.KarmaCalculator;
import org.mamute.validators.AnsweredByValidator;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.hibernate.extra.Load;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;

@Routed
@Controller
public class AnswerController {
	@Inject private Result result;
	@Inject private AnswerDAO answers;
	@Inject private LoggedUser currentUser;
    @Inject private KarmaCalculator calculator;
	@Inject private MessageFactory messageFactory;
	@Inject private Validator validator;
	@Inject private AnsweredByValidator answeredByValidator;
	@Inject private NotificationManager notificationManager;
	@Inject private WatcherDAO watchers;
	@Inject private ReputationEventDAO reputationEvents;
	@Inject private BundleFormatter bundle;
	@Inject private BrutalValidator brutalValidator;
	@Inject private AttachmentDao attachments;

	@Get
	@CustomBrutauthRules(EditAnswerRule.class)
	public void answerEditForm(@Load Answer answer) {
		result.include("answer",  answer);
	}

	@Post
	@CustomBrutauthRules(EditAnswerRule.class)
	public void edit(@Load Answer original, MarkedText description, String comment, List<Long> attachmentsIds) {
		AnswerInformation information = new AnswerInformation(description, currentUser, comment);
		brutalValidator.validate(information);
		
		validator.onErrorRedirectTo(this).answerEditForm(original);
		
		UpdateStatus status = original.updateWith(information);
		answers.save(original);
		List<Attachment> attachmentsLoaded = attachments.load(attachmentsIds);
		original.removeAttachments();
		original.add(attachmentsLoaded);

		result.include("mamuteMessages", Arrays.asList(messageFactory.build("confirmation", status.getMessage())));
		Question originalQuestion = original.getMainThread();
		result.redirectTo(QuestionController.class).showQuestion(originalQuestion, originalQuestion.getSluggedTitle());
	}
	
	@Post
	@CustomBrutauthRules({LoggedRule.class, InputRule.class, InactiveQuestionRequiresMoreKarmaRule.class})
	public void newAnswer(@Load Question question, MarkedText description, boolean watching, List<Long> attachmentsIds) {
		User current = currentUser.getCurrent();
		boolean canAnswer = answeredByValidator.validate(question);
		boolean isUserWithKarma = current.hasKarma();
		AnswerInformation information = new AnswerInformation(description, currentUser, "new answer");
		Answer answer  = new Answer(information, question, current);
		if (canAnswer) {
    		question.touchedBy(current);
        	answers.save(answer);
        	ReputationEvent reputationEvent = new ReputationEvent(EventType.CREATED_ANSWER, question, current);
        	reputationEvents.save(reputationEvent);

    		if (isUserWithKarma) {
				current.increaseKarma(reputationEvent.getKarmaReward());
			}

			result.redirectTo(QuestionController.class).showQuestion(question, question.getSluggedTitle());
			notificationManager.sendEmailsAndInactivate(new EmailAction(answer, question));
			if (watching) {
				watchers.add(question, new Watcher(current));
			}
			List<Attachment> attachmentsLoaded = attachments.load(attachmentsIds);
			answer.add(attachmentsLoaded);
        } else {
        	result.include("answer", answer);
        	answeredByValidator.onErrorRedirectTo(QuestionController.class).showQuestion(question, question.getSluggedTitle());
        }
		
	}
	
	@Post
	public void markAsSolution(Long solutionId) {
		Answer solution = answers.getById(solutionId);
		Question question = solution.getMainThread();
        if (!currentUser.getCurrent().isAuthorOf(question)) {
			result.use(Results.status()).forbidden(bundle.getMessage("answer.error.not_autor"));
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
