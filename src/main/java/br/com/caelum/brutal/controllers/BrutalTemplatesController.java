package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.dao.PaginatableDAO;
import br.com.caelum.brutal.dao.WithUserDAO.OrderType;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class BrutalTemplatesController {

	private final Result result;

	public BrutalTemplatesController(Result result) {
		this.result = result;
	}
	
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
