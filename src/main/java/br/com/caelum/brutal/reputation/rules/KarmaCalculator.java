package br.com.caelum.brutal.reputation.rules;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Information;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class KarmaCalculator {

    static final int SOLVED_QUESTION_AUTHOR = 5;
    static final int SOLUTION_AUTHOR = 20;
    static final int ANSWER_VOTED_UP = 10;
    static final int MY_ANSWER_VOTED_DOWN = -2;
    static final int MY_QUESTION_VOTED_DOWN = MY_ANSWER_VOTED_DOWN;
    static final int QUESTION_VOTED_UP = 5;
    static final int APPROVED_INFORMATION = 2;
    static final int COMMENT_VOTED_UP = 1;
	static final int ASKED_QUESTION = 2;
	static final int ANSWERED_QUESTION = 2;
	static final int DOWNVOTED_QUESTION_OR_ANSWER = -2;
    
    public int karmaFor(KarmaRewardEvent voteEvent) {
    	return voteEvent.reward();
    }
    
    /**
     * karma won by the author of a answer marked as solution 
     * @param solution the answer marked as solution
     */
    public int karmaForSolutionAuthor(Answer solution) {
        if (solution.isTheSameAuthorOfQuestion()) {
            return 0;
        }
        return SOLUTION_AUTHOR;
    }
    
    /**
     * karma won by the author of a question that was marked as solved
     * @param solution the answer marked as solution
     */
    public int karmaForAuthorOfQuestionSolved(Answer solution) {
        if (solution.isTheSameAuthorOfQuestion()) {
            return 0;
        }
        return SOLVED_QUESTION_AUTHOR;
    }

    public int karmaForApprovedInformation(Information approved) {
        return APPROVED_INFORMATION;
    }
    
}
