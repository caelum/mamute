package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;
import static br.com.caelum.vraptor.view.Results.json;
import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.brutauth.AccessLevel;
import br.com.caelum.brutal.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOrKarmaRule;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.infra.ModelUrlMapping;
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
	private final ModelUrlMapping mapping;

	public VoteController(Result result, User currentUser, 
			VoteDAO voteDAO, VotingMachine votingMachine, ModelUrlMapping mapping) {
		this.result = result;
		this.currentUser = currentUser;
		this.votes = voteDAO;
        this.votingMachine = votingMachine;
		this.mapping = mapping;
	}

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.VOTE_UP)
	@Post("/{type}/{id}/voto/positivo")
	public void voteUp(Long id, String type) {
		tryToVoteVotable(id, VoteType.UP, mapping.getClassFor(type));
	}

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.VOTE_DOWN)
	@Post("/{type}/{id}/voto/negativo")
	public void voteDown(Long id, String type) {
		tryToVoteVotable(id, VoteType.DOWN, mapping.getClassFor(type));
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