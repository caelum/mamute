package br.com.caelum.brutal.controllers;

import java.util.List;

import br.com.caelum.brutal.auth.ModeratorAccess;
import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.AnswerInformationDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.QuestionInformationDAO;
import br.com.caelum.brutal.dao.UpdatableInformationDAO;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.Updatable;
import br.com.caelum.brutal.model.UpdatablesAndPendingHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

@Resource
public class HistoryController {

	private final Result result;
	private final QuestionInformationDAO questionEdits;
    private final QuestionDAO questions;
    private final User currentUser;
    private final AnswerInformationDAO answerEdits;
    private final UpdatableInformationDAO updatables;
    private final AnswerDAO answers;
    private final Validator validator;

	public HistoryController(Result result, QuestionInformationDAO edits, QuestionDAO questions, 
	        User currentUser, AnswerInformationDAO answerEdits, UpdatableInformationDAO updatables, AnswerDAO answers, Validator validator) {
		this.result = result;
		this.questionEdits = edits;
        this.questions = questions;
        this.currentUser = currentUser;
        this.answerEdits = answerEdits;
        this.updatables = updatables;
        this.answers = answers;
        this.validator = validator;
	}

	@ModeratorAccess
	@Get("/history")
	public void unmoderated() {
		UpdatablesAndPendingHistory pendingQuestions = questionEdits.pendingByUpdatables();
		UpdatablesAndPendingHistory pendingAnswers = answerEdits.pendingByUpdatables();
		result.include("pendingQuestions", pendingQuestions);
		result.include("pendingAnswers", pendingAnswers);
	}

	@ModeratorAccess
	@Get("/questions/history/{questionId}/similar")
	public void similarQuestions(Long questionId) {
		result.include("histories", updatables.pendingFor(questionId, Question.class));
		result.include("question", questions.getById(questionId));
	}
	
	@ModeratorAccess
	@Get("/answers/history/{answerId}/similar")
	public void similarAnswers(Long answerId) {
	    result.include("histories", updatables.pendingFor(answerId, Answer.class));
	}

    @ModeratorAccess
    @Post("/questions/published/{questionId}/{aprovedHistoryId}")
    public void publishQuestion(Long questionId, Long aprovedHistoryId) {
        Information approved = updatables.getUpdatableInfoById(aprovedHistoryId, QuestionInformation.class);
        Updatable updatable = updatables.getUpdatableById(questionId, Question.class);
        List<Information> pending = updatables.pendingFor(questionId, Question.class);
        
        
        approve(aprovedHistoryId, approved, updatable, pending);
        result.redirectTo(this).unmoderated();
    }
    
    @ModeratorAccess
    @Post("/answers/published/{answerId}/{aprovedHistoryId}")
    public void publishAnswer(Long answerId, Long aprovedHistoryId) {
        Information approved = updatables.getUpdatableInfoById(aprovedHistoryId, AnswerInformation.class);
        Updatable updatable = updatables.getUpdatableById(answerId, Answer.class);
        List<Information> pending = updatables.pendingFor(answerId, Answer.class);
        
        
        approve(aprovedHistoryId, approved, updatable, pending);
        result.redirectTo(this).unmoderated();
    }

    private void approve(Long aprovedHistoryId, Information approved, Updatable updatable,
            List<Information> pending) {
        if (!approved.isPending()) {
            result.use(Results.http()).sendError(403);
            return;
        }
        
        refusePending(aprovedHistoryId, pending);
        currentUser.approve(updatable, approved);
        
    }
    
    private void refusePending(Long aprovedHistoryId, List<Information> pending) {
        for (Information refused : pending) {
	        if (!refused.getId().equals(aprovedHistoryId)) {
	            refused.moderate(currentUser, UpdateStatus.REFUSED);
	        }
        }
    }
	
}
