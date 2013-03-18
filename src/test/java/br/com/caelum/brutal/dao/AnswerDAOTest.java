package br.com.caelum.brutal.dao;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.SubscribableDTO;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;

public class AnswerDAOTest extends DatabaseTestCase {

    private User questionAuthor = user("guilherme", "email@email.com");
    private User answerAuthor1 = user("leonardo", "answer2@email.com");
    private User answerAuthor2 = user("francisco", "answer1@email.com");
    private User answerAuthor3 = user("felipe", "answer3@email.com");
    private QuestionBuilder question = new QuestionBuilder();
    private Tag defaultTag = tag("defaultTag");
    private Question creativeQuestion = question.withTag(defaultTag).withTitle("title title title title").withDescription("description descriptions descriptions descriptions descriptions").withAuthor(questionAuthor).build();
    AnswerDAO answers;
    
    @Before
    public void before_test() {
    	answers = new AnswerDAO(session);
        session.save(questionAuthor);
        session.save(answerAuthor2);
        session.save(answerAuthor3);
        session.save(answerAuthor1);
        session.save(defaultTag);
        session.save(creativeQuestion);
    }

    @Test
    public void should_find_recent_answers() {
        DateTimeUtils.setCurrentMillisFixed(new DateTime().minusHours(4).getMillis());
        Answer oldAnswer1 = answer("answer answer answer answer answer answer", creativeQuestion, answerAuthor1);
        Answer oldAnswer2 = answer("answer answer answer answer answer answer", creativeQuestion, answerAuthor1);
        
        DateTimeUtils.setCurrentMillisSystem();
        Answer newAnswer1 = answer("answer answer answer answer answer answer", creativeQuestion, answerAuthor2);
        Answer newAnswer2 = answer("answer answer answer answer answer answer", creativeQuestion, answerAuthor3);
        
        session.save(oldAnswer1);
        session.save(oldAnswer2);
        session.save(newAnswer1);
        session.save(newAnswer2);
        
        Long milisecAgo = (long) (3 * (60 * 60 * 1000));
        DateTime threeHoursAgo = new DateTime(System.currentTimeMillis() - milisecAgo);
        List<SubscribableDTO> recentAnswers = answers.getSubscribablesAfter(threeHoursAgo);
        
        assertEquals(6, recentAnswers.size());
        assertEquals(creativeQuestion.getId(), recentAnswers.get(0).getQuestion().getId());
    }
    
}
