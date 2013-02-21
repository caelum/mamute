package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;
import static br.com.caelum.vraptor.view.Results.json;
import br.com.caelum.brutal.auth.LoggedAccess;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;
import br.com.caelum.brutal.model.VotingMachine;
import br.com.caelum.brutal.model.interfaces.Votable;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class VoteController {

	private final Result result;
	private final User currentUser;
	private final VoteDAO votes;
	private final VotingMachine votingMachine;

	public VoteController(Result result, User currentUser, VoteDAO voteDAO, VotingMachine votingMachine) {
		this.result = result;
		this.currentUser = currentUser;
		this.votes = voteDAO;
        this.votingMachine = votingMachine;
	}

	@LoggedAccess
	@Post("/question/{id}/up")
	public void voteQuestionUp(Long id) {
		tryToVoteQuestion(id, VoteType.UP, Question.class);
	}

	@LoggedAccess
	@Post("/question/{id}/down")
	public void voteQuestionDown(Long id) {
		tryToVoteQuestion(id, VoteType.DOWN, Question.class);
	}

	@LoggedAccess
	@Post("/answer/{id}/up")
	public void voteAnswerUp(Long id) {
		tryToVoteQuestion(id, VoteType.UP, Answer.class);
	}

	@LoggedAccess
	@Post("/answer/{id}/down")
	public void voteAnswerDown(Long id) {
		tryToVoteQuestion(id, VoteType.DOWN, Answer.class);
	}

	@SuppressWarnings("rawtypes")
	private void tryToVoteQuestion(Long id, VoteType voteType, Class votableType) {
		try {
		    Votable votable = votes.loadVotedOnFor(votableType, id);
		    Vote current = new Vote(currentUser, voteType);
		    votingMachine.register(votable, current, votableType);
		    votes.save(current);
		    result.use(json()).withoutRoot().from(votable.getVoteCount()).serialize();
		} catch (IllegalArgumentException e) {
		    result.use(http()).sendError(403);
		    return;
        }
	}
}