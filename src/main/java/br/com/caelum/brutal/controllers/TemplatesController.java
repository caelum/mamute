package br.com.caelum.brutal.controllers;

import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class TemplatesController {

	private final Result result;

	public TemplatesController(Result result) {
		this.result = result;
	}
	
	public void comment(){}
}
