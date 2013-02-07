package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.ModeratorAccess;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.QuestionInformationDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionAndPendingHistory;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

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
		QuestionAndPendingHistory pending = histories.pending();
		result.include("pendingQuestionsEntrySet", pending.questionsEntrySet());
	}

	@ModeratorAccess
	@Get("/history/{id}/similar")
	public void similar(Long id) {
		result.include("histories", histories.from(id));
	}
	
	@ModeratorAccess
	@Post("/history/{questionId}/{historyId}")
	public void publish(Long questionId, Long historyId) {
	    QuestionInformation approvedEdit = histories.getById(historyId);
	    Question question = questions.getById(questionId);
	    question.aprove(approvedEdit, currentUser);
	    result.redirectTo(QuestionController.class).showQuestion(question.getId(), question.getSluggedTitle());
	}

}
