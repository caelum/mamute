package br.com.caelum.brutal.controllers;

import java.util.Arrays;
import java.util.List;

import br.com.caelum.brutal.auth.ModeratorAccess;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.QuestionInformationDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.UpdatablesAndPendingHistory;
import br.com.caelum.brutal.model.QuestionInformation;
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
	private final QuestionInformationDAO histories;
    private final QuestionDAO questions;
    private final User currentUser;

	public HistoryController(Result result, QuestionInformationDAO edits, QuestionDAO questions, User currentUser) {
		this.result = result;
		this.histories = edits;
        this.questions = questions;
        this.currentUser = currentUser;
	}

	@ModeratorAccess
	@Get("/history")
	public void unmoderated() {
		UpdatablesAndPendingHistory pending = histories.pendingByUpdatables();
		result.include("pendingQuestionsEntrySet", pending.questionsEntrySet());
	}

	@ModeratorAccess
	@Get("/history/{questionId}/similar")
	public void similar(Long questionId) {
		result.include("histories", histories.pendingFrom(questionId));
	}

    @ModeratorAccess
    @Post("/history/{questionId}/{aprovedHistoryId}")
    public void publish(Long questionId, Long aprovedHistoryId) {
        QuestionInformation approvedEdit = histories.getById(aprovedHistoryId);
        if (!approvedEdit.isPending()) {
            result.use(Results.http()).sendError(403);
            return;
        }

        Question question = questions.getById(questionId);
        List<QuestionInformation> pending = histories.pendingFrom(questionId);
        refusePending(aprovedHistoryId, pending);
        question.aprove(approvedEdit, currentUser);

        result.redirectTo(this).unmoderated();
    }


    private void refusePending(Long aprovedHistoryId, List<QuestionInformation> pending) {
        for (QuestionInformation refused : pending) {
	        if (!refused.getId().equals(aprovedHistoryId)) {
	            refused.moderate(currentUser, UpdateStatus.REFUSED);
	        }
        }
    }
	
	@ModeratorAccess
	@Post("/history/{historyId}")
	public void refuse(Long historyId) {
	    QuestionInformation refusedEdit = histories.getById(historyId);
	    if (!refusedEdit.isPending()) {
	        result.use(Results.http()).sendError(403);
	        return;
	    } 
	    refusedEdit.moderate(currentUser, UpdateStatus.REFUSED);
	    result.include("confirmations", Arrays.asList("history.refused.successfully"));
	    result.redirectTo(this).similar(refusedEdit.getQuestion().getId());
	}

}
