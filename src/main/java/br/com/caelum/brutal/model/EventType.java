package br.com.caelum.brutal.model;

import static java.util.Arrays.asList;

import java.util.List;

import br.com.caelum.brutal.reputation.rules.KarmaCalculator;
import br.com.caelum.brutal.reputation.rules.KarmaRewardEvent;

public enum EventType implements KarmaRewardEvent {
	
	QUESTION_UPVOTE {
		@Override
		public Integer reward() {
			return KarmaCalculator.MY_QUESTION_VOTED_UP;
		}
	}, 
	
	
	QUESTION_DOWNVOTE {
		@Override
		public Integer reward() {
			return KarmaCalculator.MY_QUESTION_VOTED_DOWN;
		}
	}, 
	
	ANSWER_UPVOTE {
		@Override
		public Integer reward() {
			return KarmaCalculator.MY_ANSWER_VOTED_UP;
		}
	},
	
	ANSWER_DOWNVOTE {
		@Override
		public Integer reward() {
			return KarmaCalculator.MY_ANSWER_VOTED_DOWN;
		}
	}, 
	
	COMMENT_UPVOTE {
		@Override
		public Integer reward() {
			return KarmaCalculator.COMMENT_VOTED_UP;
		}
	}, 
	
	EDIT_APPROVED {
		@Override
		public Integer reward() {
			return KarmaCalculator.APPROVED_INFORMATION;
		}
	}, 
	
	CREATED_ANSWER {
		@Override
		public Integer reward() {
			return KarmaCalculator.ASKED_QUESTION;
		}
	}, 
	
	CREATED_QUESTION {
		@Override
		public Integer reward() {
			return KarmaCalculator.ANSWERED_QUESTION;
		}
	}, 
	DOWNVOTED_SOMETHING {
		@Override
		public Integer reward() {
			return KarmaCalculator.DOWNVOTED_QUESTION_OR_ANSWER;
		}
	}, 
	IGNORED {
		@Override
		public Integer reward() {
			return 0;
		}
	}, 
	SOLVED_QUESTION {
		@Override
		public Integer reward() {
			return KarmaCalculator.SOLUTION_AUTHOR;
		}
	}, 
	MARKED_SOLUTION {
		@Override
		public Integer reward() {
			return KarmaCalculator.SOLVED_QUESTION_AUTHOR;
		}
	};

	public abstract Integer reward();
	
	public static List<EventType> ANSWERER_RELATED_EVENTS() {
		return asList(ANSWER_DOWNVOTE, ANSWER_UPVOTE, SOLVED_QUESTION);
		
	}

}
