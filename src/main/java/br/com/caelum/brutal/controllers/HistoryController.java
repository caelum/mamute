package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.LoggedAccess;
import br.com.caelum.brutal.dao.QuestionInformationDAO;
import br.com.caelum.brutal.model.QuestionAndPendingHistory;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class HistoryController {

	private final Result result;
	private final QuestionInformationDAO histories;

	public HistoryController(Result result, QuestionInformationDAO edits) {
		this.result = result;
		this.histories = edits;
	}

	@LoggedAccess
	@Get("/history")
	public void unmoderated() {
		QuestionAndPendingHistory pending = histories.pending();
		result.include("pendingQuestionsEntrySet", pending.questionsEntrySet());
	}


	@LoggedAccess
	@Get("/history/{id}/similar")
	public void similar(Long id) {
		result.include("histories", histories.allSimilarTo(id));
	}

}
