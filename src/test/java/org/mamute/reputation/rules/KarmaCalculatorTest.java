package org.mamute.reputation.rules;

import static org.junit.Assert.assertEquals;
import static org.mamute.reputation.rules.KarmaCalculator.MY_ANSWER_VOTED_DOWN;
import static org.mamute.reputation.rules.KarmaCalculator.MY_ANSWER_VOTED_UP;
import static org.mamute.reputation.rules.KarmaCalculator.MY_QUESTION_VOTED_DOWN;
import static org.mamute.reputation.rules.KarmaCalculator.MY_QUESTION_VOTED_UP;

import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.TestCase;
import org.mamute.model.Answer;
import org.mamute.model.Question;
import org.mamute.model.User;
import org.mamute.model.VoteType;
import org.mamute.reputation.rules.KarmaCalculator;
import org.mamute.reputation.rules.ReceivedVoteEvent;

public class KarmaCalculatorTest extends TestCase {
    private KarmaCalculator karmaCalculator = new KarmaCalculator();
    private User questionAuthor = user("chico", "chico@brutal", 1l);
    private User answerAuthor = user("answerauthor", "answer@brutal", 2l);
    private Question question = new QuestionBuilder().withAuthor(questionAuthor).build();
    private Answer answer = answer("answer description", question, answerAuthor);

    @Test
    public void should_calculate_karma_for_votes() {
        assertEquals(MY_QUESTION_VOTED_UP, karmaCalculator.karmaFor(new ReceivedVoteEvent(VoteType.UP, question, null, true).reputationEvent()));
        assertEquals(MY_QUESTION_VOTED_DOWN, karmaCalculator.karmaFor(new ReceivedVoteEvent(VoteType.DOWN, answer, null, true).reputationEvent()));

        assertEquals(MY_ANSWER_VOTED_UP, karmaCalculator.karmaFor(new ReceivedVoteEvent(VoteType.UP, answer, null, true).reputationEvent()));
        assertEquals(MY_ANSWER_VOTED_DOWN, karmaCalculator.karmaFor(new ReceivedVoteEvent(VoteType.DOWN, question, null, true).reputationEvent()));
    }

}
