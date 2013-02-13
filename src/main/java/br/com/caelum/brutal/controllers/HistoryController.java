package br.com.caelum.brutal.controllers;

import java.util.List;

import br.com.caelum.brutal.auth.ModeratorAccess;
import br.com.caelum.brutal.dao.AnswerInformationDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.QuestionInformationDAO;
import br.com.caelum.brutal.dao.UpdatableInformationDAO;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.UpdatableInformation;
import br.com.caelum.brutal.model.UpdatablesAndPendingHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class HistoryController {

	private final Result result;
	private final QuestionInformationDAO questionEdits;
    private final QuestionDAO questions;
    private final User currentUser;
    private final AnswerInformationDAO answerEdits;
    private final UpdatableInformationDAO updatables;

	public HistoryController(Result result, QuestionInformationDAO edits, QuestionDAO questions, 
	        User currentUser, AnswerInformationDAO answerEdits, UpdatableInformationDAO updatables) {
		this.result = result;
		this.questionEdits = edits;
        this.questions = questions;
        this.currentUser = currentUser;
        this.answerEdits = answerEdits;
        this.updatables = updatables;
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
	}
	
	@ModeratorAccess
	@Get("/answers/history/{answerId}/similar")
	public void similarAnswers(Long answerId) {
	    result.include("histories", updatables.pendingFor(answerId, Answer.class));
	}

    @ModeratorAccess
    @Post("/history/{questionId}/{aprovedHistoryId}")
    public void publish(Long questionId, Long aprovedHistoryId) {
        QuestionInformation approvedEdit = questionEdits.getById(aprovedHistoryId);
        if (!approvedEdit.isPending()) {
            result.use(Results.http()).sendError(403);
            return;
        }

        Question question = questions.getById(questionId);
        List<UpdatableInformation> pending = updatables.pendingFor(questionId, Question.class);
        refusePending(aprovedHistoryId, pending);
        question.aprove(approvedEdit, currentUser);

        result.redirectTo(this).unmoderated();
    }

    private void refusePending(Long aprovedHistoryId, List<UpdatableInformation> pending) {
        for (UpdatableInformation refused : pending) {
	        if (!refused.getId().equals(aprovedHistoryId)) {
	            refused.moderate(currentUser, UpdateStatus.REFUSED);
	        }
        }
    }
	
}
