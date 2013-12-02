package br.com.caelum.brutal.controllers;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOrKarmaRule;
import br.com.caelum.brutal.dao.InformationDAO;
import br.com.caelum.brutal.dao.ModeratableDao;
import br.com.caelum.brutal.dao.ReputationEventDAO;
import br.com.caelum.brutal.infra.ModelUrlMapping;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.ModeratableAndPendingHistory;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;
import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Controller
public class HistoryController {

	@Inject private Result result;
    @Inject private LoggedUser currentUser;
    @Inject private InformationDAO informations;
	@Inject private ModeratableDao moderatables;
    @Inject private KarmaCalculator calculator;
	@Inject private ModelUrlMapping urlMapping;
	@Inject private ReputationEventDAO reputationEvents;

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.MODERATE_EDITS)
	@Get("/historico")
	public void history() {
		ModeratableAndPendingHistory pendingQuestions = informations.pendingByUpdatables(Question.class);			
		result.include("questions", pendingQuestions);

		ModeratableAndPendingHistory pendingAnswers = informations.pendingByUpdatables(Answer.class);
		result.include("answers", pendingAnswers);
	}

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.MODERATE_EDITS)
	@Get("/historico/{moderatableType}")
	public void unmoderated(String moderatableType) {
		result.redirectTo(this).history();
	}
	
	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.MODERATE_EDITS)
	@Get("/historico/resposta/{moderatableId}/versoes")
	public void similarAnswers(Long moderatableId) {
		similar("resposta", moderatableId);
	}
	
	
	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.MODERATE_EDITS)
	@Get("/historico/pergunta/{moderatableId}/versoes")
	public void similarQuestions(Long moderatableId) {
		similar("pergunta", moderatableId);
	}
	
	@Get("/{questionId}/historico")
	public void questionHistory(Long questionId) {
		result.include("histories", informations.historyForQuestion(questionId));
		result.include("post", moderatables.getById(questionId, Question.class));
		result.include("userMediumPhoto", true);
		result.include("isHistoryQuestion", true);
	}

	private void similar(String moderatableType, Long moderatableId) {
		Class<?> clazz = urlMapping.getClassFor(moderatableType);
		result.include("histories", informations.pendingFor(moderatableId, clazz));
		result.include("post", moderatables.getById(moderatableId, clazz));
		result.include("type", moderatableType);
		result.include("userMediumPhoto", true);
		result.include("isHistoryQuestion", false);
	}

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.MODERATE_EDITS)
    @Post("/publicar/{moderatableType}")
    public void publish(Long moderatableId, String moderatableType, Long aprovedInformationId,  String aprovedInformationType) {
    	Class<?> moderatableClass = urlMapping.getClassFor(moderatableType);
    	Information approved = informations.getById(aprovedInformationId, aprovedInformationType);
    	
        Moderatable moderatable = moderatables.getById(moderatableId, moderatableClass);
        List<Information> pending = informations.pendingFor(moderatableId, moderatableClass);
        
        if (!approved.isPending()) {
        	result.use(Results.http()).sendError(403);
        	return;
        }
        
        User approvedAuthor = approved.getAuthor();
        refusePending(aprovedInformationId, pending);
        currentUser.getCurrent().approve(moderatable, approved);
        ReputationEvent editAppoved = new ReputationEvent(EventType.EDIT_APPROVED, moderatable.getQuestion(), approvedAuthor);
        int karma = calculator.karmaFor(editAppoved);
        approvedAuthor.increaseKarma(karma);
        reputationEvents.save(editAppoved);
        
        result.redirectTo(this).history();
    }
	
	@Post("/rejeitar/{typeName}/{informationId}")
	public void reject(Long informationId, String typeName) {
		Information informationRefused = informations.getById(informationId, typeName);
		informationRefused.moderate(currentUser.getCurrent(), UpdateStatus.REFUSED);
		Long moderatableId = informationRefused.getModeratable().getId();
		if (typeName.equals(AnswerInformation.class.getSimpleName())) {
			result.redirectTo(this).similarAnswers(moderatableId);
		} else if (typeName.equals(QuestionInformation.class.getSimpleName())) {
			result.redirectTo(this).similarQuestions(moderatableId);
		}
	}

    private void refusePending(Long aprovedHistoryId, List<Information> pending) {
        for (Information refused : pending) {
	        if (!refused.getId().equals(aprovedHistoryId)) {
	            refused.moderate(currentUser.getCurrent(), UpdateStatus.REFUSED);
	        }
        }
    }
    
	
}
