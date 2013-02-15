package br.com.caelum.brutal.controllers;

import java.util.List;

import br.com.caelum.brutal.auth.ModeratorAccess;
import br.com.caelum.brutal.dao.InformationDAO;
import br.com.caelum.brutal.dao.ModeratableDao;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.UpdatablesAndPendingHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class HistoryController {

	private final Result result;
    private final QuestionDAO questions;
    private final User currentUser;
    private final InformationDAO informations;
	private final ModeratableDao moderatables;

	public HistoryController(Result result, QuestionDAO questions, User currentUser,
			InformationDAO informations, ModeratableDao moderatables) {
		this.result = result;
        this.questions = questions;
        this.currentUser = currentUser;
        this.informations = informations;
		this.moderatables = moderatables;
	}

	@ModeratorAccess
	@Get("/history")
	public void unmoderated() {
		UpdatablesAndPendingHistory pendingQuestions = informations.pendingByUpdatables(Question.class);
		UpdatablesAndPendingHistory pendingAnswers = informations.pendingByUpdatables(Answer.class);
		result.include("pendingQuestions", pendingQuestions);
		result.include("pendingAnswers", pendingAnswers);
	}

	@ModeratorAccess
	@Get("/questions/history/{questionId}/similar")
	public void similarQuestions(Long questionId) {
		result.include("histories", informations.pendingFor(questionId, Question.class));
		result.include("question", questions.getById(questionId));
	}
	
	@ModeratorAccess
	@Get("/answers/history/{answerId}/similar")
	public void similarAnswers(Long answerId) {
	    result.include("histories", informations.pendingFor(answerId, Answer.class));
		result.include("answer", moderatables.getById(answerId, Answer.class));
	}

    @ModeratorAccess
    @Post("/publish/{moderatableType}")
    public void publish(Long moderatableId, String moderatableType, Long aprovedInformationId,  String aprovedInformationType) throws ClassNotFoundException {
    	Class<?> moderatableClass = Class.forName("br.com.caelum.brutal.model." + moderatableType);
		Class<?> aprovedInformationClass = Class.forName("br.com.caelum.brutal.model." + aprovedInformationType);
    	
    	Information approved = informations.getById(aprovedInformationId, aprovedInformationClass);
    	Moderatable moderatable = moderatables.getById(moderatableId, moderatableClass);
    	List<Information> pending = informations.pendingFor(moderatableId, moderatableClass);
    	
    	if (!approved.isPending()) {
    	    result.use(Results.http()).sendError(403);
    	    return;
    	}
    	
    	refusePending(aprovedInformationId, pending);
    	currentUser.approve(moderatable, approved);
    	
    	result.redirectTo(this).unmoderated();
    }

    private void refusePending(Long aprovedHistoryId, List<Information> pending) {
        for (Information refused : pending) {
	        if (!refused.getId().equals(aprovedHistoryId)) {
	            refused.moderate(currentUser, UpdateStatus.REFUSED);
	        }
        }
    }
	
}
