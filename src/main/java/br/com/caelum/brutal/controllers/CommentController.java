package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;
import static br.com.caelum.vraptor.view.Results.status;
import br.com.caelum.brutal.auth.rules.MinimumReputation;
import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.infra.ModelUrlMapping;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.validators.CommentValidator;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class CommentController {

	private final Result result;
	private final User currentUser;
	private final CommentDAO comments;
	private final CommentValidator validator;
	private final ModelUrlMapping urlMapping;

	public CommentController(Result result, User currentUser, CommentDAO comments, CommentValidator validator, ModelUrlMapping urlMapping) {
		this.result = result;
		this.currentUser = currentUser;
		this.comments = comments;
		this.validator = validator;
		this.urlMapping = urlMapping;
	}

	@MinimumReputation(PermissionRulesConstants.CREATE_COMMENT)
	@Post("/{onWhat}/{id}/comentar")
	public void comment(Long id, String onWhat, String message) {
		Comment comment = new Comment(currentUser, message);
		Class<?> type = getType(onWhat);
		if (type == null) {
			result.notFound();
			return;
		}
		if (validator.validate(comment)) {
			comments.load(type, id).add(comment);
			comments.save(comment);
			result.redirectTo(TemplatesController.class).comment(comment);
		}
		validator.onErrorUse(http()).body("<span class=\"error\">error</span>");
	}

	@Post("/comentario/editar/{id}")
	public void edit(String comment, Long id) {
		Comment original = comments.getById(id);
		if (original.getAuthor().getId() != currentUser.getId()) {
			result.use(status()).badRequest("comment.edit.not_author");
			return;
		}
		if (validator.validate(comment)) {
			original.setComment(comment);
			comments.save(original);
			result.forwardTo(TemplatesController.class).comment(original);
		}
		validator.onErrorUse(http()).body("<span class=\"error\">error</span>");
	}
	
	private Class<?> getType(String name) {
		try {
			return urlMapping.getClassFor(name);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
