package org.mamute.reputation.rules;

import org.mamute.model.ReputationEvent;

public class KarmaCalculator {

    public static final int SOLVED_QUESTION_AUTHOR = 5;
    public static final int SOLUTION_AUTHOR = 20;
    public static final int MY_ANSWER_VOTED_UP = 10;
    public static final int MY_ANSWER_VOTED_DOWN = -2;
    public static final int MY_QUESTION_VOTED_DOWN = MY_ANSWER_VOTED_DOWN;
    public static final int MY_QUESTION_VOTED_UP = 5;
    public static final int APPROVED_INFORMATION = 2;
    public static final int COMMENT_VOTED_UP = 1;
	public static final int ASKED_QUESTION = 2;
	public static final int ANSWERED_QUESTION = 2;
	public static final int DOWNVOTED_QUESTION_OR_ANSWER = -2;
	public static final int MY_NEWS_VOTED_UP = 5;
	public static final int MY_NEWS_VOTED_DOWN = -2;
    
    public int karmaFor(ReputationEvent voteEvent) {
    	return voteEvent.getKarmaReward();
    }

}
