package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;
import static br.com.caelum.vraptor.view.Results.json;
import br.com.caelum.brutal.auth.rules.MinimumReputation;
import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
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

	@MinimumReputation(PermissionRulesConstants.VOTE_UP)
	@Post("/pergunta/{id}/voto/positivo")
	public void voteQuestionUp(Long id) {
		tryToVoteVotable(id, VoteType.UP, Question.class);
	}

	@MinimumReputation(PermissionRulesConstants.VOTE_DOWN)
	@Post("/pergunta/{id}/voto/negativo")
	public void voteQuestionDown(Long id) {
		tryToVoteVotable(id, VoteType.DOWN, Question.class);
	}

	@MinimumReputation(PermissionRulesConstants.VOTE_UP)
	@Post("/resposta/{id}/voto/positivo")
	public void voteAnswerUp(Long id) {
		tryToVoteVotable(id, VoteType.UP, Answer.class);
	}

	@MinimumReputation(PermissionRulesConstants.VOTE_DOWN)
	@Post("/resposta/{id}/voto/negativo")
	public void voteAnswerDown(Long id) {
		tryToVoteVotable(id, VoteType.DOWN, Answer.class);
	}
	
	@MinimumReputation(PermissionRulesConstants.VOTE_UP)
	@Post("/comentario/{id}/voto/positivo")
	public void voteCommentUp(Long id) {
		tryToVoteVotable(id, VoteType.UP, Comment.class);
	}

	@SuppressWarnings("rawtypes")
	private void tryToVoteVotable(Long id, VoteType voteType, Class votableType) {
		try {
		    Votable votable = votes.loadVotable(votableType, id);
		    Vote current = new Vote(currentUser, voteType);
		    votingMachine.register(votable, current, votableType);
		    votes.save(current);
		    result.use(json()).withoutRoot().from(votable.getVoteCount()).serialize();
		} catch (IllegalArgumentException e) {
		    result.use(http()).sendError(409);
		    return;
        }
	}
}