package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;
import br.com.caelum.brutal.auth.Logged;
import br.com.caelum.brutal.dao.QuestionInformationDAO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class HistoryController {

	private final Result result;
	private final User currentUser;
	private final QuestionInformationDAO histories;

	public HistoryController(Result result, User currentUser, QuestionInformationDAO edits) {
		this.result = result;
		this.currentUser = currentUser;
		this.histories = edits;
	}

	@Logged
	@Get("/history")
	public void list() {
		if (!currentUser.isModerator()) {
			result.use(http()).sendError(403);
			return;
		}
		result.include("histories", histories.unmoderated());
	}


	@Logged
	@Get("/history/${id}/similar")
	public void similar(long id) {
		if (!currentUser.isModerator()) {
			result.use(http()).sendError(403);
			return;
		}
		result.include("histories", histories.allSimilarTo(id));
	}

}
