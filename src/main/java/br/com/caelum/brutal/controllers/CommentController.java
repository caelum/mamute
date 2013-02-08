package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;
import static br.com.caelum.vraptor.view.Results.json;
import static br.com.caelum.vraptor.view.Results.status;
import br.com.caelum.brutal.auth.LoggedAccess;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.view.Results;

@Resource
public class CommentController {

	private final Result result;
	private final User currentUser;
	private final CommentDAO comments;

	public CommentController(Result result, User currentUser, CommentDAO comments) {
		this.result = result;
		this.currentUser = currentUser;
		this.comments = comments;
	}

	@SuppressWarnings("rawtypes")
	@LoggedAccess
	@Post("/{onWhat}/{id}/comment")
	public void comment(Long id, String onWhat, String message) throws ClassNotFoundException {
		Class type = Class.forName("br.com.caelum.brutal.model." + onWhat);
		Comment comment = comments.load(type, id).add(new Comment(currentUser, message));
		comments.save(comment);
		result.use(http()).body("<li class=\"comment\">" + message + "</li>");
	}
	

	@Post("/Comment/edit/{id}")
	public void edit(String comment, Long id) {
		Comment original = comments.getById(id);
		if(original.getAuthor().getId() != currentUser.getId()){
			result.use(status()).badRequest("comment.edit.not_author");
			return;
		}
		original.setComment(comment);
		comments.save(original);
		result.use(json()).withoutRoot().from(comment).serialize();
	}


}
