package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;
import br.com.caelum.brutal.auth.Logged;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

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
	@Logged
	@Post("/{onWhat}/{id}/comment")
	public void comment(Long id, String onWhat, String message) throws ClassNotFoundException {
		Class type = Class.forName("br.com.caelum.brutal.model." + onWhat);
		Comment comment = comments.load(type, id).add(new Comment(currentUser, message));
		comments.save(comment);
		result.use(http()).body("<li class=\"comment\">" + message + "</li>");
	}

}
