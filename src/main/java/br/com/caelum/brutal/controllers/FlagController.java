package br.com.caelum.brutal.controllers;

import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class FlagController {
	
	private final Result result;

	public FlagController(Result result) {
		this.result = result;
	}

	@Post("/comments/{commentId}/flags")
	public void addFlag(Long commentId, String flagType) {
		System.out.println(commentId);
		result.nothing();
	}
}
