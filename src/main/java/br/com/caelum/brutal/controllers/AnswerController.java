package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.Logged;
import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AnswerController {
	private final Result result;
	private final AnswerDAO dao;
	private final User currentUser;

	public AnswerController(Result result, AnswerDAO dao, User currentUser) {
		this.result = result;
		this.dao = dao;
		this.currentUser = currentUser;
	}

	@Post("/question/answer/{question.id}")
	@Logged
	public void newAnswer(Question question, Answer answer) {
		dao.create(answer, question, currentUser);
        result.redirectTo(QuestionController.class).
        			showQuestion(answer.getQuestion().getId(), 
        						 answer.getQuestion().getSluggedTitle());
	}
}
