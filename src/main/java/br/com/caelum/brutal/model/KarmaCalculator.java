package br.com.caelum.brutal.model;

import br.com.caelum.brutal.model.interfaces.Votable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class KarmaCalculator {

    static final int SOLVED_QUESTION_AUTHOR = 5;
    static final int SOLUTION_AUTHOR = 20;
    static final int ANSWER_VOTED_UP = 10;
    static final int ANSWER_VOTED_DOWN = -2;
    static final int QUESTION_VOTED_DOWN = ANSWER_VOTED_DOWN;
    static final int QUESTION_VOTED_UP = 5;
    static final int APPROVED_INFORMATION = 2;
    /**
     *  karma won by the author of a votable that received a vote of 
     *  a given type 
     */
    public int karmaFor(VoteType type, Votable votable) {
        if (votable.getType() == Question.class) {
            return karmaForQuestion(type);
        } 
        return karmaForAnswer(type);
    }

    private int karmaForQuestion(VoteType type) {
        return type == VoteType.UP ? QUESTION_VOTED_UP : QUESTION_VOTED_DOWN;
    }

    private int karmaForAnswer(VoteType type) {
        return type == VoteType.UP ? ANSWER_VOTED_UP : ANSWER_VOTED_DOWN;
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
