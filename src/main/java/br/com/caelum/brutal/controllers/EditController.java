package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;
import br.com.caelum.brutal.auth.Logged;
import br.com.caelum.brutal.dao.HistoryDAO;
import br.com.caelum.brutal.model.MarkDown;
import br.com.caelum.brutal.model.Updatable;
import br.com.caelum.brutal.model.UpdateHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class EditController {

	private final Result result;
	private final User currentUser;
	private final HistoryDAO edits;

	public EditController(Result result, User currentUser, HistoryDAO edits) {
		this.result = result;
		this.currentUser = currentUser;
		this.edits = edits;
	}

	@Logged
	@Post("/{onWhat}/{id}/edit/{field}")
	public void edit(Long id, String onWhat, String field, String value)
			throws ClassNotFoundException {
		String parsed = MarkDown.parse(value);

		Class<?> type = Class.forName("br.com.caelum.brutal.model." + onWhat);
		Updatable object = edits.load(type, id);
		
		boolean isTheAuthor = object.getAuthor().getId().equals(currentUser.getId());
		
		if(!isTheAuthor) {
			if(currentUser.isModerator() || currentUser.getKarma() > 10) {
				createHistoryFor(value, type, field, UpdateStatus.PENDING, object);
				result.use(http()).setStatusCode(201);
			} else {
				result.use(http()).sendError(403);
			}
			return;
		}
		
		if (!object.update(field, value)) {
			result.use(http()).sendError(403);
			return;
		}
		
		createHistoryFor(value, type, field, UpdateStatus.NO_NEED_TO_APPROVE, object);
		result.use(http()).body(parsed);
	}

	private void createHistoryFor(String value, Class<?> type, String field, UpdateStatus status, Updatable object) {
		UpdateHistory history = new UpdateHistory(value, type, field, currentUser, status, object.getId());
		edits.save(history);
	}

}
