package br.com.caelum.brutal.controllers;

import static br.com.caelum.brutal.dao.WithUserDAO.OrderType.ByVotes;
import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.dao.WithUserDAO.OrderType;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.User;
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

	public void paginateQuestion(QuestionDAO questions, User author, OrderType order, Integer page) {
		result.include("questionsByVotes", questions.withAuthorBy(author, ByVotes, 1));
		result.include("questionsCount", questions.countWithAuthor(author));
		result.include("totalPages", questions.numberOfPagesTo(author));
		result.include("currentPage", page);
	}

	public void paginateAnswer(AnswerDAO answers, User author, OrderType order, Integer page) {
		result.include("answers", answers.withAuthorBy(author, order, page));
		result.include("answersCount", answers.countWithAuthor(author));
		result.include("totalPages", answers.numberOfPagesTo(author));
		result.include("currentPage", page);
		result.include("type", "respostas");
	}

	public void paginateWatch(WatcherDAO watchers, User user, Integer page) {
		result.include("watchedQuestions", watchers.questionsWatchedBy(user, 1));
		result.include("watchedQuestionsCount", watchers.countWithAuthor(user));
		result.include("totalPages", watchers.numberOfPagesTo(user));
		result.include("currentPage", page);
	}
}
