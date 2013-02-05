package br.com.caelum.brutal.components;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import br.com.caelum.brutal.dao.NotifiableDAO;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Notifiable;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;

public class NotificationMailerJobTest {

    @SuppressWarnings("unchecked")
    @Test
    public void should_get_notifiables_by_email() {
        NotifiableDAO<Notifiable> dao = mock(NotifiableDAO.class);
        NotificationMailerJob mailerJob = new NotificationMailerJob(dao);
        User author = new User("felipe", "felipe@gmail.com", "12123123");
        User responder1 = new User("chico", "chico@gmail.com", "12123123");
        User responder2 = new User("joviane", "joviane@gmail.com", "12123123");
        
        Question question = new Question("title title title title", "description descriptions descriptions descriptions descriptions", author);
        
        Answer newAnswer1 = new Answer("answer answer answer answer answer answer", question, responder1);
        question.add(newAnswer1);
        Answer newAnswer2 = new Answer("answer answer answer answer answer answer", question, responder2);
        question.add(newAnswer2);
        
        
        List<Notifiable> notifiables = Arrays.asList((Notifiable)newAnswer1, (Notifiable)newAnswer2);
        
        Map<String, List<Notifiable>> notifiablesByEmail = mailerJob.notifiablesByEmail(notifiables);
        
        assertEquals(2, notifiablesByEmail.get("felipe@gmail.com").size());
        assertEquals(1, notifiablesByEmail.get("joviane@gmail.com").size());
        assertEquals(1, notifiablesByEmail.get("chico@gmail.com").size());
    }

}
