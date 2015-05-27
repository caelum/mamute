package org.mamute.controllers;

import javax.inject.Inject;

import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.hibernate.extra.Load;
import org.mamute.auth.rules.PermissionRules;
import org.mamute.brutauth.auth.rules.EnvironmentKarmaRule;
import org.mamute.brutauth.auth.rules.InactiveQuestionRequiresMoreKarmaRule;
import org.mamute.brutauth.auth.rules.InputRule;
import org.mamute.dao.CommentDAO;
import org.mamute.dao.WatcherDAO;
import org.mamute.infra.ModelUrlMapping;
import org.mamute.infra.NotFoundException;
import org.mamute.mail.action.EmailAction;
import org.mamute.model.*;
import org.mamute.model.interfaces.Watchable;
import org.mamute.model.watch.Watcher;
import org.mamute.notification.NotificationManager;
import org.mamute.validators.CommentValidator;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.view.Results;

import static br.com.caelum.vraptor.view.Results.http;

@Routed
@Controller
public class CommentController {

	@Inject private Result result;
	@Inject private CommentDAO comments;
	@Inject private CommentValidator validator;
	@Inject private ModelUrlMapping urlMapping;
	@Inject private LoggedUser currentUser;
	@Inject private NotificationManager notificationManager;
	@Inject private WatcherDAO watchers;
	@Inject private Environment environment;

	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	@EnvironmentAccessLevel(PermissionRules.CREATE_COMMENT)
	@CustomBrutauthRules({InputRule.class, InactiveQuestionRequiresMoreKarmaRule.class})
	@Post
	public void comment(Long id, String onWhat, MarkedText comment, boolean watching) {
		User current = currentUser.getCurrent();
		Comment newComment = new Comment(current, comment);
		Class<?> type = getType(onWhat);
		
		validator.validate(newComment);
		validator.onErrorUse(Results.http()).setStatusCode(400);
		
		org.mamute.model.Post commentable = comments.loadCommentable(type, id);
		commentable.add(newComment);
		comments.save(newComment);
		Watchable watchable = commentable.getMainThread();
		notificationManager.sendEmailsAndInactivate(new EmailAction(newComment, commentable));
		if (watching) {
			watchers.add(watchable, new Watcher(current));
		} else {
			watchers.removeIfWatching(watchable, new Watcher(current));
		}

		result.include("post", commentable);
		result.include("type", onWhat);
    	result.forwardTo(BrutalTemplatesController.class).comment(newComment);
	}

	@Post
	public void edit(Long id, MarkedText comment) {
		Comment original = comments.getById(id);
		if (!currentUser.getCurrent().isAuthorOf(original)) {
			result.use(Results.status()).badRequest("comment.edit.not_author");
			return;
		}
		if (validator.validate(comment.getPure())) {
			original.setComment(comment);
			comments.save(original);
			result.forwardTo(BrutalTemplatesController.class).comment(original);
		}
		validator.onErrorUse(Results.http()).setStatusCode(400);
	}

	@Delete
	public void delete(Long commentId, String onWhat, Long postId) {
		Comment comment = comments.getById(commentId);
		if (!environment.supports("deletable.comments") || comment == null) {
			result.notFound();
			return;
		}
		if (!currentUser.isModerator() && !comment.hasAuthor(currentUser.getCurrent())) {
			result.use(http()).sendError(403);
			return;
		}
		Class<?> type = urlMapping.getClassFor(onWhat);
		org.mamute.model.Post post = comments.loadCommentable(type, postId);
		post.deleteComment(comment);
		comments.delete(comment);
		result.use(Results.referer()).redirect();
	}
	
	private Class<?> getType(String name) {
		try {
			return urlMapping.getClassFor(name);
		} catch (IllegalArgumentException e) {
			throw new NotFoundException(e);
		}
	}
}
