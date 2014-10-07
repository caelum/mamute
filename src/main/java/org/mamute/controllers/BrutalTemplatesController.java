package org.mamute.controllers;

import java.util.List;

import javax.inject.Inject;

import org.mamute.dao.PaginatableDAO;
import org.mamute.dao.WithUserPaginatedDAO.OrderType;
import org.mamute.model.Comment;
import org.mamute.model.Question;
import org.mamute.model.SanitizedText;
import org.mamute.model.User;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;

@Controller
public class BrutalTemplatesController {

	@Inject private Result result;

	
	public void comment(Comment comment){
		result.include("comment", comment);
	}

	public void userProfilePagination(PaginatableDAO paginatable, User author, OrderType order, Integer page, String type) {
		result.include("posts", paginatable.ofUserPaginatedBy(author, order, page));
		result.include("totalPages", paginatable.numberOfPagesTo(author));
		result.include("currentPage", page);
		result.include("order", order.toString());
		result.include("type", type);
	}

	public void questionSuggestion(SanitizedText query, List<Question> questions){
		result.include("questions", questions);
		result.include("query", query.getText());
	}
}
