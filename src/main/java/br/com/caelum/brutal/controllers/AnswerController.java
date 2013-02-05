package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.Logged;
import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AnswerController {
	private final Result result;
	private final AnswerDAO answers;
	private final User currentUser;
	private final QuestionDAO questions;

	public AnswerController(Result result, AnswerDAO dao, User currentUser, QuestionDAO questions) {
		this.result = result;
		this.answers = dao;
		this.currentUser = currentUser;
		this.questions = questions;
	}


	@Get("/answer/edit/{id}")
	@Logged
	public void answerEditForm(Long id) {
		result.include("answer",  answers.getById(id));
	}

	@Post("/answer/edit/{id}")
	@Logged
	public void edit(String description, Long id) {
		AnswerInformation information = new AnswerInformation(description, currentUser);

		Answer original = answers.getById(id);
		UpdateStatus status = original.updateWith(information);
		result.include("status", status);
	}
	
	@Post("/question/answer/{question.id}")
	@Logged
	public void newAnswer(Question question, String answerText) {
        Question loadedQuestion = questions.getById(question.getId());
        loadedQuestion.touchedBy(currentUser);

        AnswerInformation information = new AnswerInformation(answerText, currentUser);
		Answer answer  = new Answer(information, question, currentUser);
		answers.save(answer);
        
        result.redirectTo(QuestionController.class).showQuestion(loadedQuestion.getId(),
                loadedQuestion.getSluggedTitle());
	}
	
	@Post("/question/answer/markAsSolution/{solutionId}")
	public void markAsSolution(Long solutionId) {
		Answer solution = answers.getById(solutionId);
		solution.markAsSolution();
		result.nothing();
	}
}
