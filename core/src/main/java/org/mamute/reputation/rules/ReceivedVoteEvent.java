package org.mamute.reputation.rules;

import java.util.HashMap;
import java.util.Map;

import org.mamute.model.Answer;
import org.mamute.model.Comment;
import org.mamute.model.EventType;
import org.mamute.model.News;
import org.mamute.model.Question;
import org.mamute.model.ReputationEvent;
import org.mamute.model.ReputationEventContext;
import org.mamute.model.VoteType;
import org.mamute.model.interfaces.Votable;

public class ReceivedVoteEvent {
	
	private static final Map<Class<? extends Votable>, VotableRule> map = new HashMap<>();
	private final VotableRule rule;
	private final VoteType type;
	private final Votable votable;
	private final ReputationEventContext eventContext;
	private final boolean shouldCountKarma;
	{
		map.put(Question.class, new QuestionVoteRule());
		map.put(Answer.class, new AnswerVoteRule());
		map.put(Comment.class, new CommentVoteRule());
		map.put(News.class, new NewsVoteRule());
	}
	
	public ReceivedVoteEvent(VoteType type, Votable votable, ReputationEventContext eventContext, boolean shouldCountKarma) {
		this.type = type;
		this.votable = votable;
		this.eventContext = eventContext;
		this.shouldCountKarma = shouldCountKarma;
		this.rule = map.get(votable.getType());
	}
	
	public ReputationEvent reputationEvent() {
		if (shouldCountKarma) {
			EventType eventType = rule.eventType(type);
			return new ReputationEvent(eventType, eventContext, votable.getAuthor());
		}
		return new ReputationEvent(EventType.MASSIVE_VOTE_IGNORED, eventContext, votable.getAuthor());
	}
	
	
}
