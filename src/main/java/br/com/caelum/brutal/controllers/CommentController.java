package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;
import static br.com.caelum.vraptor.view.Results.status;
import br.com.caelum.brutal.auth.rules.MinimumReputation;
import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.infra.ModelUrlMapping;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.validators.CommentValidator;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class CommentController {

	private final Result result;
	private final CommentDAO comments;
	private final CommentValidator validator;
	private final ModelUrlMapping urlMapping;
	private final LoggedUser currentUser;

	public CommentController(Result result, LoggedUser currentUser, CommentDAO comments, CommentValidator validator, ModelUrlMapping urlMapping) {
		this.result = result;
		this.currentUser = currentUser;
		this.comments = comments;
		this.validator = validator;
		this.urlMapping = urlMapping;
	}

	@MinimumReputation(PermissionRulesConstants.CREATE_COMMENT)
	@Post("/{onWhat}/{id}/comentar")
	public void comment(Long id, String onWhat, String comment) {
		Comment newComment = new Comment(currentUser.getCurrent(), comment);
		Class<?> type = getType(onWhat);
		if (type == null) {
			result.notFound();
			return;
		}
		if (validator.validate(newComment)) {
			comments.load(type, id).add(newComment);
			comments.save(newComment);
			result.forwardTo(BrutalTemplatesController.class).comment(newComment);
		}
		validator.onErrorUse(http()).setStatusCode(400);
	}

	@Post("/comentario/editar/{id}")
	public void edit(Long id, String comment) {
		Comment original = comments.getById(id);
		if (!currentUser.getCurrent().isAuthorOf(original)) {
			result.use(status()).badRequest("comment.edit.not_author");
			return;
		}
		if (validator.validate(comment)) {
			original.setComment(comment);
			comments.save(original);
			result.forwardTo(BrutalTemplatesController.class).comment(original);
		}
		validator.onErrorUse(http()).setStatusCode(400);
	}
	
	private Class<?> getType(String name) {
		try {
			return urlMapping.getClassFor(name);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
