package org.mamute.model;

import static java.util.Arrays.asList;

import java.util.List;

import org.mamute.reputation.rules.KarmaCalculator;
import org.mamute.reputation.rules.KarmaRewardEvent;

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
	},
	MASSIVE_VOTE_IGNORED {
		@Override
		public Integer reward() {
			return 0;
		}
	},
	MASSIVE_VOTE_REVERTED {
		@Override
		public Integer reward() {
			return karma;
		}
	},
	NEWS_UPVOTE {
		@Override
		public Integer reward() {
			return KarmaCalculator.MY_NEWS_VOTED_UP;
		}
	},
	NEWS_DOWNVOTE {
		@Override
		public Integer reward() {
			return KarmaCalculator.MY_NEWS_VOTED_DOWN;
		}
	};

	private static int karma;

	public abstract Integer reward();
	
	public static List<EventType> ANSWERER_RELATED_EVENTS() {
		return asList(CREATED_ANSWER, ANSWER_DOWNVOTE, ANSWER_UPVOTE, SOLVED_QUESTION);
	}
	
	public static List<EventType> ASKER_RELATED_EVENTS() {
		return asList(CREATED_QUESTION, QUESTION_DOWNVOTE, QUESTION_UPVOTE, MARKED_SOLUTION);
	}

	public void setKarma(int karma) {
		EventType.karma = karma;
	}
	
}
