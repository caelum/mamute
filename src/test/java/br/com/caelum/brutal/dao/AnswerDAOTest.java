package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integracao.dao.DatabaseTestCase;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerAndSubscribedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;

public class AnswerDAOTest extends DatabaseTestCase {

    private User questionAuthor = new User("name", "email", "12345");
    private User answerAuthor = new User("name", "answer", "12345");
    @Before
    public void before_test() {
        session.save(questionAuthor);
        session.save(answerAuthor);
    }

    @Test
    public void should_find_recent_answers() {
        AnswerDAO notifiableDAO = new AnswerDAO(session);
        Question question = new Question("title title title title", "description descriptions descriptions descriptions descriptions", questionAuthor);
        
        DateTimeUtils.setCurrentMillisFixed(new DateTime().minusHours(4).getMillis());
        Answer oldAnswer1 = new Answer("answer answer answer answer answer answer", question, answerAuthor);
        Answer oldAnswer2 = new Answer("answer answer answer answer answer answer", question, answerAuthor);
        
        DateTimeUtils.setCurrentMillisSystem();
        Answer newAnswer1 = new Answer("answer answer answer answer answer answer", question, answerAuthor);
        Answer newAnswer2 = new Answer("answer answer answer answer answer answer", question, answerAuthor);
        
        session.save(question);
        session.save(oldAnswer1);
        session.save(oldAnswer2);
        session.save(newAnswer1);
        session.save(newAnswer2);
        
        
        List<AnswerAndSubscribedUser> recentAnswers = notifiableDAO.getRecentAnswersAndSubscribedUsers(3);
        assertEquals(2, recentAnswers.size());
        recentAnswers.get(0).getUser().getId().equals(questionAuthor.getId());
        recentAnswers.get(1).getUser().getId().equals(questionAuthor.getId());
        recentAnswers.get(0).getAnswer().getId().equals(newAnswer1.getId());
        recentAnswers.get(1).getAnswer().getId().equals(newAnswer2.getId());
    }
    
}
