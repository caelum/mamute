package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;

public class KarmaCalculatorTest extends TestCase {

    @Test
    public void should_calculate_karma_for_votes() {
        KarmaCalculator karmaCalculator = new KarmaCalculator();
        User author = user("chico", "chico@brutal");
        User answerAuthor = user("answerauthor", "answer@brutal");
        Question question = new QuestionBuilder().withAuthor(author).build();
        Answer answer = answer("answer description", question, answerAuthor);
        
        assertEquals(5, karmaCalculator.karmaFor(VoteType.UP, question));
        assertEquals(-2, karmaCalculator.karmaFor(VoteType.DOWN, answer));
        
        assertEquals(10, karmaCalculator.karmaFor(VoteType.UP, answer));
        assertEquals(-2, karmaCalculator.karmaFor(VoteType.DOWN, question));
    }

}
