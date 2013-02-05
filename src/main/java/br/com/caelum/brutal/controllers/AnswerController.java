package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.Logged;
import br.com.caelum.brutal.dao.AnswerDAO;
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

	public AnswerController(Result result, AnswerDAO dao, User currentUser) {
		this.result = result;
		this.answers = dao;
		this.currentUser = currentUser;
	}


	@Get("/answer/edit/{id}")
	@Logged
	public void answerEditForm(Long id) {
		result.include("answer",  answers.getById(id));
	}

	@Post("/question/answer/{question.id}")
	@Logged
	public void newAnswer(Question question, String answerText) {
        Question loadedQuestion = questions.loadById(question.getId());
        loadedQuestion.touchedBy(currentUser);

        AnswerInformation information = new AnswerInformation(answerText, currentUser);
		information.setInitStatus(UpdateStatus.NO_NEED_TO_APPROVE);
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
