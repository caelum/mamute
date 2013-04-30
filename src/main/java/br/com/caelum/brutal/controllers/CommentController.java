package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;
import static br.com.caelum.vraptor.view.Results.status;
import br.com.caelum.brutal.auth.rules.MinimumReputation;
import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.dao.WatchDAO;
import br.com.caelum.brutal.infra.ModelUrlMapping;
import br.com.caelum.brutal.mail.action.EmailAction;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.watch.Watch;
import br.com.caelum.brutal.notification.NotificationManager;
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
	private final NotificationManager notificationManager;
	private final WatchDAO watches;

	public CommentController(Result result, LoggedUser currentUser, CommentDAO comments,
			CommentValidator validator, ModelUrlMapping urlMapping,
			NotificationManager notificationManager, WatchDAO watches) {
		this.result = result;
		this.currentUser = currentUser;
		this.comments = comments;
		this.validator = validator;
		this.urlMapping = urlMapping;
		this.notificationManager = notificationManager;
		this.watches = watches;
	}

	@MinimumReputation(PermissionRulesConstants.CREATE_COMMENT)
	@Post("/{onWhat}/{id}/comentar")
	public void comment(Long id, String onWhat, String comment) {
		User current = currentUser.getCurrent();
		Comment newComment = new Comment(current, comment);
		Class<?> type = getType(onWhat);
		if (type == null) {
			result.notFound();
			return;
		}
		if (validator.validate(newComment)) {
			br.com.caelum.brutal.model.Post commentable = comments.load(type, id);
			commentable.add(newComment);
			comments.save(newComment);
			result.forwardTo(BrutalTemplatesController.class).comment(newComment);
			Question question = commentable.getQuestion();
			notificationManager.sendEmailsAndActivate(new EmailAction(newComment, commentable, question));
        	watches.add(new Watch(current, question));
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
