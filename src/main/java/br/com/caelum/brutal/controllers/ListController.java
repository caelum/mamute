package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ListController {

	private final QuestionDAO questions;
	private final Result result;

	public ListController(QuestionDAO questions, Result result) {
		this.questions = questions;
		this.result = result;
	}

	public void home() {
		result.include("questions", questions.all());
	}

}
