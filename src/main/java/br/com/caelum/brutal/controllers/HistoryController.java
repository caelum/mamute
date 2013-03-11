package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;

import java.util.List;

import br.com.caelum.brutal.auth.ModeratorAccess;
import br.com.caelum.brutal.dao.InformationDAO;
import br.com.caelum.brutal.dao.ModeratableDao;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.Question;
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

	private static final String MODEL_PACKAGE = "br.com.caelum.brutal.model.";
	private final Result result;
    private final User currentUser;
    private final InformationDAO informations;
	private final ModeratableDao moderatables;
    private final KarmaCalculator calculator;

	public HistoryController(Result result, User currentUser,
			InformationDAO informations, ModeratableDao moderatables, KarmaCalculator calculator) {
		this.result = result;
        this.currentUser = currentUser;
        this.informations = informations;
		this.moderatables = moderatables;
        this.calculator = calculator;
	}
	
	@ModeratorAccess
	@Get("/history")
	public void history() {
		result.redirectTo(this).unmoderated(Question.class.getSimpleName().toLowerCase());
	}

	@ModeratorAccess
	@Get("/history/{moderatableType}")
	public void unmoderated(String moderatableType) {
		try {
			String first = moderatableType.substring(0, 1);
			String moderatableTypeFormatted = moderatableType.replaceFirst(first, first.toUpperCase());
			
			Class<?> clazz = Class.forName(MODEL_PACKAGE + moderatableTypeFormatted);
			UpdatablesAndPendingHistory pending = informations.pendingByUpdatables(clazz);
			
			result.include("pending", pending);
			result.include("type", moderatableType);
		} catch (ClassNotFoundException e) {
			result.notFound();
		}
	}

	@ModeratorAccess
	@Get("/history/question/{questionId}/similar")
	public void similarQuestions(Long questionId) {
		result.include("histories", informations.pendingFor(questionId, Question.class));
		result.include("question", moderatables.getById(questionId, Question.class));
	}
	
	@ModeratorAccess
	@Get("/history/answer/{answerId}/similar")
	public void similarAnswers(Long answerId) {
	    result.include("histories", informations.pendingFor(answerId, Answer.class));
		result.include("answer", moderatables.getById(answerId, Answer.class));
	}

    @ModeratorAccess
    @Post("/publish/{moderatableType}")
    public void publish(Long moderatableId, String moderatableType, Long aprovedInformationId,  String aprovedInformationType) {
        try {
        	Class<?> moderatableClass = Class.forName(MODEL_PACKAGE + moderatableType);
        	Class<?> aprovedInformationClass = Class.forName(MODEL_PACKAGE + aprovedInformationType);
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
        } catch (ClassNotFoundException e) {
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
