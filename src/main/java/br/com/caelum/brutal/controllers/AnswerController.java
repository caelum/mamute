package br.com.caelum.brutal.controllers;

import java.util.Arrays;

import br.com.caelum.brutal.auth.LoggedAccess;
import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.KarmaCalculator;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.view.Results;

@Resource
public class AnswerController {
	private final Result result;
	private final AnswerDAO answers;
	private final QuestionDAO questions;
	private final LoggedUser currentUser;
	private final Localization localization;
    private final KarmaCalculator calculator;

	public AnswerController(Result result, AnswerDAO dao, User currentUser, 
	        QuestionDAO questions, LoggedUser user, Localization localization,
	        KarmaCalculator calculator) {
		this.result = result;
		this.answers = dao;
		this.currentUser = user;
		this.questions = questions;
		this.localization = localization;
        this.calculator = calculator;
	}


	@Get("/answer/edit/{id}")
	public void answerEditForm(Long id) {
		result.include("answer",  answers.getById(id));
	}

	@Post("/answer/edit/{id}")
	public void edit(String description, Long id, String comment) {
		AnswerInformation information = new AnswerInformation(description, currentUser, comment);

		Answer original = answers.getById(id);
		UpdateStatus status = original.updateWith(information);
		answers.save(original);

		result.include("confirmations", Arrays.asList(status.getMessage()));
		Question originalQuestion = original.getQuestion();
		result.redirectTo(QuestionController.class).showQuestion(originalQuestion.getId(), originalQuestion.getSluggedTitle());
	}
	
	@Post("/question/answer/{question.id}")
	@LoggedAccess
	public void newAnswer(Question question, String description) {
        Question loadedQuestion = questions.getById(question.getId());
        loadedQuestion.touchedBy(currentUser.getCurrent());

        AnswerInformation information = new AnswerInformation(description, currentUser, "new");
		Answer answer  = new Answer(information, question, currentUser.getCurrent());
		answers.save(answer);
        
        result.redirectTo(QuestionController.class).showQuestion(loadedQuestion.getId(),
                loadedQuestion.getSluggedTitle());
	}
	
	@Post("/question/answer/markAsSolution/{solutionId}")
	public void markAsSolution(Long solutionId) {
		Answer solution = answers.getById(solutionId);
		Question question = solution.getQuestion();
        if (currentUser.getCurrent().isAuthorOf(question)) {
            if (question.isSolved()) {
                decreaseKarmaOfOldSolution(question);
            }
		    solution.markAsSolution();
		    increaseKarmaOfUsersInvolved(solution);
			result.nothing();
		} else {
			result.use(Results.status()).forbidden(localization.getMessage("answer.error.not_autor"));
			result.redirectTo(QuestionController.class).showQuestion(question.getId(),
	                question.getSluggedTitle());
		}
	}

    private void decreaseKarmaOfOldSolution(Question question) {
        Answer solution = question.getSolution();
        User author = solution.getAuthor();
        author.descreaseKarma(calculator.karmaForSolutionAuthor(solution));
    }


    private void increaseKarmaOfUsersInvolved(Answer solution) {
        int karmaForSolutionAuthor = calculator.karmaForSolutionAuthor(solution);
        int karmaForQuestionAuthorSolution = calculator.karmaForAuthorOfQuestionSolved(solution);
        solution.getAuthor().increaseKarma(karmaForSolutionAuthor);
        solution.getQuestion().getAuthor().increaseKarma(karmaForQuestionAuthorSolution);
    }


}
