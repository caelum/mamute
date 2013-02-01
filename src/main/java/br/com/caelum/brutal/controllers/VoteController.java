package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.Logged;
import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Votable;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class VoteController {

	private final Result result;
    private final QuestionDAO questions;
    private final User currentUser;
    private final VoteDAO voteDAO;
    private final AnswerDAO answers;

	public VoteController(Result result, QuestionDAO questions, User currentUser, VoteDAO voteDAO, AnswerDAO answers) {
		this.result = result;
        this.questions = questions;
        this.currentUser = currentUser;
        this.voteDAO = voteDAO;
        this.answers = answers;
	}
	
	@Logged
	@Post("/question/{id}/up")
	public void voteQuestionUp(Long id) {
	    addVote(questions.getById(id), VoteType.UP);
		result.use(Results.http()).setStatusCode(200);
	}

	@Logged
	@Post("/question/{id}/down")
	public void voteQuestionDown(Long id) {
	    addVote(questions.getById(id), VoteType.DOWN);
	    result.use(Results.http()).setStatusCode(200);
	}
	
	@Logged
	@Post("/answer/{id}/up")
	public void voteAnswerUp(Long id) {
	    addVote(answers.getById(id), VoteType.UP);
	    result.use(Results.http()).setStatusCode(200);
	}
	
	@Logged
	@Post("/answer/{id}/down")
	public void voteAnswerDown(Long id) {
	    addVote(answers.getById(id), VoteType.DOWN);
	    result.use(Results.http()).setStatusCode(200);
	}
	
	private void addVote(Votable votable, VoteType type) {
	    Vote vote = new Vote(currentUser, type);
	    votable.addVote(vote);
	    voteDAO.save(vote);
	}
}
