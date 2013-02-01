package br.com.caelum.brutal.controllers;

import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class VoteController {

	private final Result result;

	public VoteController(Result result) {
		this.result = result;
	}
	
	@Post("/{type}/{id}/{vote}")
	public void vote(String type, Integer id, String vote) {
		result.use(Results.http()).setStatusCode(200);
	}
	
}
