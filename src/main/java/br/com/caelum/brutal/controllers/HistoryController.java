package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;

import java.util.List;

import javax.annotation.Nullable;

import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOrKarmaRule;
import br.com.caelum.brutal.dao.InformationDAO;
import br.com.caelum.brutal.dao.ModeratableDao;
import br.com.caelum.brutal.dao.ReputationEventDAO;
import br.com.caelum.brutal.infra.ModelUrlMapping;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.ModeratableAndPendingHistory;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;
import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class HistoryController {

	private final Result result;
    private final User currentUser;
    private final InformationDAO informations;
	private final ModeratableDao moderatables;
    private final KarmaCalculator calculator;
	private final ModelUrlMapping urlMapping;
	private final ReputationEventDAO reputationEvents;

	public HistoryController(Result result, @Nullable User currentUser,
			InformationDAO informations, ModeratableDao moderatables,
			KarmaCalculator calculator, ModelUrlMapping urlMapping,
			ReputationEventDAO reputationEvents) {
		this.result = result;
        this.currentUser = currentUser;
        this.informations = informations;
		this.moderatables = moderatables;
        this.calculator = calculator;
		this.urlMapping = urlMapping;
		this.reputationEvents = reputationEvents;
	}
	

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.MODERATE_EDITS)
	@Get("/historico")
	public void history() {
		result.redirectTo(this).unmoderated("pergunta");
	}

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.MODERATE_EDITS)
	@Get("/historico/{moderatableType}")
	public void unmoderated(String moderatableType) {
		try{
			Class<?> clazz = urlMapping.getClassFor(moderatableType);
			ModeratableAndPendingHistory pending = informations.pendingByUpdatables(clazz);
			
			result.include("pending", pending);
			result.include("type", moderatableType);
		}catch(IllegalArgumentException e){
			result.notFound();
		}
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
	
	private void similar(String moderatableType, Long moderatableId) {
		Class<?> clazz = urlMapping.getClassFor(moderatableType);
		result.include("histories", informations.pendingFor(moderatableId, clazz));
		result.include("post", moderatables.getById(moderatableId, clazz));
		result.include("type", moderatableType);
		result.include("userMediumPhoto", true);
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
        	result.use(http()).sendError(403);
        	return;
        }
        
        User approvedAuthor = approved.getAuthor();
        refusePending(aprovedInformationId, pending);
        currentUser.approve(moderatable, approved);
        ReputationEvent editAppoved = new ReputationEvent(EventType.EDIT_APPROVED, moderatable.getQuestion(), approvedAuthor);
        int karma = calculator.karmaFor(editAppoved);
        approvedAuthor.increaseKarma(karma);
        reputationEvents.save(editAppoved);
        
        result.redirectTo(this).unmoderated(moderatableType);
    }
	
	@Post("/rejeitar/{typeName}/{informationId}")
	public void reject(Long informationId, String typeName) {
		Information informationRefused = informations.getById(informationId, typeName);
		informationRefused.moderate(currentUser, UpdateStatus.REFUSED);
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
	            refused.moderate(currentUser, UpdateStatus.REFUSED);
	        }
        }
    }
    
	
}
