package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;

import java.util.List;

import br.com.caelum.brutal.auth.ModeratorOrKarmaAccess;
import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.dao.InformationDAO;
import br.com.caelum.brutal.dao.ModeratableDao;
import br.com.caelum.brutal.infra.ModelUrlMapping;
import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.UpdatablesAndPendingHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;
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

	public HistoryController(Result result, User currentUser,
			InformationDAO informations, ModeratableDao moderatables,
			KarmaCalculator calculator, ModelUrlMapping urlMapping) {
		this.result = result;
        this.currentUser = currentUser;
        this.informations = informations;
		this.moderatables = moderatables;
        this.calculator = calculator;
		this.urlMapping = urlMapping;
	}
	
	@ModeratorOrKarmaAccess(PermissionRulesConstants.MODERATE_EDITS)
	@Get("/historico")
	public void history() {
		result.redirectTo(this).unmoderated("pergunta");
	}

	@ModeratorOrKarmaAccess(PermissionRulesConstants.MODERATE_EDITS)
	@Get("/historico/{moderatableType}")
	public void unmoderated(String moderatableType) {
		try{
			Class<?> clazz = urlMapping.getClassFor(moderatableType);
			UpdatablesAndPendingHistory pending = informations.pendingByUpdatables(clazz);
			
			result.include("pending", pending);
			result.include("type", moderatableType);
		}catch(IllegalArgumentException e){
			result.notFound();
		}
	}

	@ModeratorOrKarmaAccess(PermissionRulesConstants.MODERATE_EDITS)
	@Get("/historico/{moderatableType}/{moderatableId}/versoes")
	public void similar(String moderatableType, Long moderatableId) {
		Class<?> clazz = urlMapping.getClassFor(moderatableType);
		result.include("histories", informations.pendingFor(moderatableId, clazz));
		result.include("question", moderatables.getById(moderatableId, clazz));
	}
	

	@ModeratorOrKarmaAccess(PermissionRulesConstants.MODERATE_EDITS)
    @Post("/publicar/{moderatableType}")
    public void publish(Long moderatableId, String moderatableType, Long aprovedInformationId,  String aprovedInformationType) {
        try {
        	Class<?> moderatableClass = urlMapping.getClassFor(moderatableType);
			Class<?> aprovedInformationClass = urlMapping.getClassFor(aprovedInformationType);
            
        	Information approved = informations.getById(aprovedInformationId, aprovedInformationClass);
            Moderatable moderatable = moderatables.getById(moderatableId, moderatableClass);
            List<Information> pending = informations.pendingFor(moderatableId, moderatableClass);
            
            if (!approved.isPending()) {
            	result.use(http()).sendError(403);
            	return;
            }
            
            refusePending(aprovedInformationId, pending);
            currentUser.approve(moderatable, approved);
            int karma = calculator.karmaForApprovedInformation(approved);
            approved.getAuthor().increaseKarma(karma);
            
            result.redirectTo(this).unmoderated(moderatableType);
        } catch (IllegalArgumentException e) {
            result.notFound();
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
