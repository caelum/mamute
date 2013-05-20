package br.com.caelum.brutal.reputation.rules;

import static br.com.caelum.brutal.reputation.rules.KarmaCalculator.MY_ANSWER_VOTED_DOWN;
import static br.com.caelum.brutal.reputation.rules.KarmaCalculator.MY_ANSWER_VOTED_UP;
import static br.com.caelum.brutal.reputation.rules.KarmaCalculator.MY_QUESTION_VOTED_DOWN;
import static br.com.caelum.brutal.reputation.rules.KarmaCalculator.MY_QUESTION_VOTED_UP;
import static br.com.caelum.brutal.reputation.rules.KarmaCalculator.SOLUTION_AUTHOR;
import static br.com.caelum.brutal.reputation.rules.KarmaCalculator.SOLVED_QUESTION_AUTHOR;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.VoteType;

public class KarmaCalculatorTest extends TestCase {
    private KarmaCalculator karmaCalculator = new KarmaCalculator();
    private User questionAuthor = user("chico", "chico@brutal", 1l);
    private User answerAuthor = user("answerauthor", "answer@brutal", 2l);
    private Question question = new QuestionBuilder().withAuthor(questionAuthor).build();
    private Answer answer = answer("answer description", question, answerAuthor);

    @Test
    public void should_calculate_karma_for_votes() {
        assertEquals(MY_QUESTION_VOTED_UP, karmaCalculator.karmaFor(new ReceivedVoteEvent(VoteType.UP, question)));
        assertEquals(MY_QUESTION_VOTED_DOWN, karmaCalculator.karmaFor(new ReceivedVoteEvent(VoteType.DOWN, answer)));

        assertEquals(MY_ANSWER_VOTED_UP, karmaCalculator.karmaFor(new ReceivedVoteEvent(VoteType.UP, answer)));
        assertEquals(MY_ANSWER_VOTED_DOWN, karmaCalculator.karmaFor(new ReceivedVoteEvent(VoteType.DOWN, question)));
    }

    @Test
    public void should_calculate_karma_for_a_solution_of_other_author() throws Exception {
        answer.markAsSolution();
        
        int karmaForSolutionAuthor = karmaCalculator.karmaForSolutionAuthor(answer);
        assertEquals(SOLUTION_AUTHOR, karmaForSolutionAuthor);
        int karmaForQuestionAuthor = karmaCalculator.karmaForAuthorOfQuestionSolved(answer);
        assertEquals(SOLVED_QUESTION_AUTHOR, karmaForQuestionAuthor);
    }
    
    @Test
    public void should_disconsider_karma_for_a_solution_from_question_author() throws Exception {
        Answer answer = answer("answer from question author", question, questionAuthor);
        answer.markAsSolution();
        
        int karmaForSolutionAuthor = karmaCalculator.karmaForSolutionAuthor(answer);
        assertEquals(0, karmaForSolutionAuthor);
        int karmaForQuestionAuthor = karmaCalculator.karmaForAuthorOfQuestionSolved(answer);
        assertEquals(0, karmaForQuestionAuthor);
    }
    
}
