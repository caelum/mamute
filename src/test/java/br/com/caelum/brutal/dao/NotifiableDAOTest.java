package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integracao.dao.DatabaseTestCase;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;

public class NotifiableDAOTest extends DatabaseTestCase {
    private User author = new User("name", "email@email", "12345");
    @Before
    public void before_test() {
        session.save(author);
    }

    @Test
    public void should_find_recent_answers() {
        NotifiableDAO<Answer> notifiableDAO = new NotifiableDAO<>(session);
        Question question = new Question("title title title title", "description descriptions descriptions descriptions descriptions", author);
        
        DateTimeUtils.setCurrentMillisFixed(new DateTime().minusHours(4).getMillis());
        Answer oldAnswer1 = new Answer("answer answer answer answer answer answer", question, author);
        Answer oldAnswer2 = new Answer("answer answer answer answer answer answer", question, author);
        
        DateTimeUtils.setCurrentMillisSystem();
        Answer newAnswer1 = new Answer("answer answer answer answer answer answer", question, author);
        Answer newAnswer2 = new Answer("answer answer answer answer answer answer", question, author);
        
        session.save(question);
        session.save(oldAnswer1);
        session.save(oldAnswer2);
        session.save(newAnswer1);
        session.save(newAnswer2);
        
        List<Answer> recentQuestions = notifiableDAO.recent(3, Answer.class);
        assertEquals(2, recentQuestions.size());
    }
    
    @Test
    public void should_find_recent_comments() throws Exception {
        NotifiableDAO<Comment> notifiableDAO = new NotifiableDAO<>(session);
        
        Comment comment = new Comment(author, "blablablablablablablablablablablabla");
        session.save(comment);
        
        List<Comment> recentQuestions = notifiableDAO.recent(3, Comment.class);
        assertEquals(1, recentQuestions.size());
        
    }

}
