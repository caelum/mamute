package org.mamute.controllers;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.mamute.auth.rules.PermissionRules;
import org.mamute.brutauth.auth.rules.EnvironmentKarmaRule;
import org.mamute.dao.AnswerDAO;
import org.mamute.dao.QuestionDAO;
import org.mamute.dao.VoteDAO;
import org.mamute.event.BadgeEvent;
import org.mamute.infra.ModelUrlMapping;
import org.mamute.model.*;
import org.mamute.model.interfaces.Votable;
import org.mamute.model.vote.VotingMachine;

import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.view.Results;

@Routed
@Controller
public class VoteController {

	@Inject private Result result;
	@Inject private LoggedUser currentUser;
	@Inject private VoteDAO votes;
	@Inject private QuestionDAO questions;
	@Inject private AnswerDAO answers;
	@Inject private VotingMachine votingMachine;
	@Inject private ModelUrlMapping mapping;
	@Inject private LoggedUser loggedUser;
	@Inject private Event<BadgeEvent> badgeEvent;
	
	@Post
	@EnvironmentAccessLevel(PermissionRules.VOTE_UP)
	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	public void voteUp(Long id, String type) {
		tryToVoteVotable(id, VoteType.UP, mapping.getClassFor(type));
		loggedUser.getCurrent().votedUp();
	}

	@Post
	@EnvironmentAccessLevel(PermissionRules.VOTE_DOWN)
	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	public void voteDown(Long id, String type) {
		tryToVoteVotable(id, VoteType.DOWN, mapping.getClassFor(type));
		
	}
	
	@Post
	@EnvironmentAccessLevel(PermissionRules.VOTE_UP)
	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	public void voteUpRemoval(Long id, String type) {
		tryToRemoveVoteVotable(id, VoteType.UP, mapping.getClassFor(type));
		loggedUser.getCurrent().votedUp();
	}

	@Post
	@EnvironmentAccessLevel(PermissionRules.VOTE_DOWN)
	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
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

			if (votableType.isAssignableFrom(Question.class)) {
				Question question = questions.getById(id);

				if (voteType == VoteType.UP) {
					badgeEvent.fire(new BadgeEvent(EventType.QUESTION_UPVOTE, currentUser.getCurrent(), question));
				} else {
					badgeEvent.fire(new BadgeEvent(EventType.QUESTION_DOWNVOTE, currentUser.getCurrent(), question));
				}
			} else if (votableType.isAssignableFrom(Answer.class)) {
				Answer answer = answers.getById(id);

				if (voteType == VoteType.UP) {
					badgeEvent.fire(new BadgeEvent(EventType.ANSWER_UPVOTE, currentUser.getCurrent(), answer));
				} else {
					badgeEvent.fire(new BadgeEvent(EventType.ANSWER_DOWNVOTE, currentUser.getCurrent(), answer));
				}
			}

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