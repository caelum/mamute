package br.com.caelum.brutal.reputation.rules;

import java.util.HashMap;
import java.util.Map;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.VoteType;
import br.com.caelum.brutal.model.interfaces.Votable;

public class ReceivedVoteEvent {
	
	private static final Map<Class<? extends Votable>, VotableRule> map = new HashMap<>();
	private final VotableRule rule;
	private final VoteType type;
	private final Votable votable;
	private final Question questionInvolved;
	private final boolean shouldCountKarma;
	{
		map.put(Question.class, new QuestionVoteRule());
		map.put(Answer.class, new AnswerVoteRule());
		map.put(Comment.class, new CommentVoteRule());
	}
	
	public ReceivedVoteEvent(VoteType type, Votable votable, Question questionInvolved, boolean shouldCountKarma) {
		this.type = type;
		this.votable = votable;
		this.questionInvolved = questionInvolved;
		this.shouldCountKarma = shouldCountKarma;
		this.rule = map.get(votable.getType());
	}
	
	public ReputationEvent reputationEvent() {
		if (shouldCountKarma) {
			EventType eventType = rule.eventType(type);
			return new ReputationEvent(eventType, questionInvolved, votable.getAuthor());
		}
		return new ReputationEvent(EventType.MASSIVE_VOTE_IGNORED, questionInvolved, votable.getAuthor());
	}
	
	
}
