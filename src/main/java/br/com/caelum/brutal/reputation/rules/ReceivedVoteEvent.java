package br.com.caelum.brutal.reputation.rules;

import java.util.HashMap;
import java.util.Map;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.VoteType;
import br.com.caelum.brutal.model.interfaces.Votable;

public class ReceivedVoteEvent implements KarmaRewardEvent {
	
	private static final Map<Class<? extends Votable>, VotableRule> map = new HashMap<>();
	private final VotableRule rule;
	private final VoteType type;
	{
		map.put(Question.class, new QuestionVoteRule());
		map.put(Answer.class, new AnswerVoteRule());
		map.put(Comment.class, new CommentVoteRule());
	}
	
	public ReceivedVoteEvent(VoteType type, Votable votable) {
		this.type = type;
		this.rule = map.get(votable.getType());
	}
	
	@Override
	public int reward() {
		return rule.calculate(type);
	}
	
	

}
