package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.model.Comment;
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
}
