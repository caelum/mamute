package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integracao.dao.DatabaseTestCase;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.SubscribableAndUser;
import br.com.caelum.brutal.model.User;

public class AnswerDAOTest extends DatabaseTestCase {

    private User questionAuthor = new User("name", "email", "12345");
    private User answerAuthor1 = new User("name", "answer2", "12345");
    private User answerAuthor2 = new User("name", "answer1", "12345");
    private User answerAuthor3 = new User("name", "answer3", "12345");
    @Before
    public void before_test() {
        session.save(questionAuthor);
        session.save(answerAuthor2);
        session.save(answerAuthor3);
        session.save(answerAuthor1);
    }

    @Test
    public void should_find_recent_answers() {
        AnswerDAO notifiableDAO = new AnswerDAO(session);
        Question question = new Question("title title title title", "description descriptions descriptions descriptions descriptions", questionAuthor);
        
        DateTimeUtils.setCurrentMillisFixed(new DateTime().minusHours(4).getMillis());
        Answer oldAnswer1 = new Answer("answer answer answer answer answer answer", question, answerAuthor1);
        Answer oldAnswer2 = new Answer("answer answer answer answer answer answer", question, answerAuthor1);
        
        DateTimeUtils.setCurrentMillisSystem();
        Answer newAnswer1 = new Answer("answer answer answer answer answer answer", question, answerAuthor2);
        Answer newAnswer2 = new Answer("answer answer answer answer answer answer", question, answerAuthor3);
        
        session.save(question);
        session.save(oldAnswer1);
        session.save(oldAnswer2);
        session.save(newAnswer1);
        session.save(newAnswer2);
        
        Long milisecAgo = (long) (3 * (60 * 60 * 1000));
        DateTime threeHoursAgo = new DateTime(System.currentTimeMillis() - milisecAgo);
        List<SubscribableAndUser> recentAnswers = notifiableDAO.getSubscribablesAfter(threeHoursAgo);
        assertEquals(6, recentAnswers.size());
    }
    
}
