package br.com.caelum.brutal.controllers;

import javax.inject.Inject;

import br.com.caelum.brutal.dao.PaginatableDAO;
import br.com.caelum.brutal.dao.WithUserPaginatedDAO.OrderType;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Result;

@Controller
public class BrutalTemplatesController {

	@Inject private Result result;

	
	public void comment(Comment comment){
		result.include("comment", comment);
	}

	public void userProfilePagination(PaginatableDAO paginatable, User author, OrderType order, Integer page, String type) {
		result.include("posts", paginatable.postsToPaginateBy(author, order, page));
		result.include("totalPages", paginatable.numberOfPagesTo(author));
		result.include("currentPage", page);
		result.include("order", order.toString());
		result.include("type", type);
	}
}
