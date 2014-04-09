package org.mamute.controllers;

import javax.inject.Inject;

import org.mamute.auth.rules.PermissionRulesConstants;
import org.mamute.brutauth.auth.rules.InactiveQuestionRequiresMoreKarmaRule;
import org.mamute.brutauth.auth.rules.InputRule;
import org.mamute.brutauth.auth.rules.ModeratorOrKarmaRule;
import org.mamute.dao.CommentDAO;
import org.mamute.dao.WatcherDAO;
import org.mamute.infra.ModelUrlMapping;
import org.mamute.infra.NotFoundException;
import org.mamute.mail.action.EmailAction;
import org.mamute.model.Comment;
import org.mamute.model.LoggedUser;
import org.mamute.model.User;
import org.mamute.model.interfaces.Watchable;
import org.mamute.model.watch.Watcher;
import org.mamute.notification.NotificationManager;
import org.mamute.validators.CommentValidator;

import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.view.Results;

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

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.CREATE_COMMENT)
	@CustomBrutauthRules({InputRule.class, InactiveQuestionRequiresMoreKarmaRule.class})
	@Post
	public void comment(Long id, String onWhat, String comment, boolean watching) {
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
    	
    	result.forwardTo(BrutalTemplatesController.class).comment(newComment);
	}

	@Post
	public void edit(Long id, String comment) {
		Comment original = comments.getById(id);
		if (!currentUser.getCurrent().isAuthorOf(original)) {
			result.use(Results.status()).badRequest("comment.edit.not_author");
			return;
		}
		if (validator.validate(comment)) {
			original.setComment(comment);
			comments.save(original);
			result.forwardTo(BrutalTemplatesController.class).comment(original);
		}
		validator.onErrorUse(Results.http()).setStatusCode(400);
	}
	
	private Class<?> getType(String name) {
		try {
			return urlMapping.getClassFor(name);
		} catch (IllegalArgumentException e) {
			throw new NotFoundException(e);
		}
	}
}
