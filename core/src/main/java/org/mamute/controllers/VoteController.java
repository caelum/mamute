package org.mamute.controllers;

import javax.inject.Inject;

import org.mamute.auth.rules.PermissionRulesConstants;
import org.mamute.brutauth.auth.rules.ModeratorOrKarmaRule;
import org.mamute.dao.VoteDAO;
import org.mamute.infra.ModelUrlMapping;
import org.mamute.model.LoggedUser;
import org.mamute.model.Vote;
import org.mamute.model.VoteType;
import org.mamute.model.interfaces.Votable;
import org.mamute.model.vote.VotingMachine;

import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Controller
public class VoteController {

	@Inject private Result result;
	@Inject private LoggedUser currentUser;
	@Inject private VoteDAO votes;
	@Inject private VotingMachine votingMachine;
	@Inject private ModelUrlMapping mapping;
	@Inject private LoggedUser loggedUser;
	
	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.VOTE_UP)
	@Post("/{type}/{id}/voto/positivo")
	public void voteUp(Long id, String type) {
		tryToVoteVotable(id, VoteType.UP, mapping.getClassFor(type));
		loggedUser.getCurrent().votedUp();
	}

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.VOTE_DOWN)
	@Post("/{type}/{id}/voto/negativo")
	public void voteDown(Long id, String type) {
		tryToVoteVotable(id, VoteType.DOWN, mapping.getClassFor(type));
		
	}
	
	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.VOTE_UP)
	@Post("/{type}/{id}/voto/remove/positivo")
	public void voteUpRemoval(Long id, String type) {
		tryToRemoveVoteVotable(id, VoteType.UP, mapping.getClassFor(type));
		loggedUser.getCurrent().votedUp();
	}

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.VOTE_DOWN)
	@Post("/{type}/{id}/voto/remove/negativo")
	public void voteDownRemoval(Long id, String type) {
		tryToRemoveVoteVotable(id, VoteType.DOWN, mapping.getClassFor(type));
		
	}

	@SuppressWarnings("rawtypes")
	private void tryToVoteVotable(Long id, VoteType voteType, Class votableType) {
		try {
		    Votable votable = votes.loadVotable(votableType, id);
		    Vote current = new Vote(currentUser.getCurrent(), voteType);
		    votingMachine.register(votable, current, votableType);
		    votes.save(current);
		    result.use(Results.json()).withoutRoot().from(votable.getVoteCount()).serialize();
		} catch (IllegalArgumentException e) {
		    result.use(Results.http()).sendError(409);
		    return;
        }
	}
	
	@SuppressWarnings("rawtypes")
	private void tryToRemoveVoteVotable(Long id, VoteType voteType, Class votableType) {
		try {
		    Votable votable = votes.loadVotable(votableType, id);
		    Vote current = new Vote(currentUser.getCurrent(), voteType);
		    votingMachine.unRegister(votable, current, votableType);
//		    votes.save(current);
		    result.use(Results.json()).withoutRoot().from(votable.getVoteCount()).serialize();
		} catch (IllegalArgumentException e) {
		    result.use(Results.http()).sendError(409);
		    return;
        }
	}
}