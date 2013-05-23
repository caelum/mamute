package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RankingController {
	
	private final Result result;
	private final UserDAO users;

	public RankingController(Result result, UserDAO users) {
		this.result = result;
		this.users = users;
	}

	@Get("/ranking")
	public void rank(Integer p) {
		int page = p == null ? 1 : p;
		result.include("topUsers", users.getRank(page));
		result.include("pages", users.numberOfPages());
		result.include("currentPage", Integer.valueOf(page));
	}
}
