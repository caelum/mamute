package br.com.caelum.brutal.notification;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.apache.commons.mail.Email;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.SubscribableDTO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Subscribable;
import br.com.caelum.brutal.vraptor.Linker;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.testing.MockTemplateMailer;

public class NotificationMailerTest extends TestCase {
    private QuestionBuilder question = new QuestionBuilder();
    private User questionAuthor = user("chico", "chico@gmail.com", 1l);
    private Question myQuestion = question.withTitle("question title").withDescription("question description").withAuthor(questionAuthor).build();
    private User answerAuthor = user("answer user", "answer@brutal.com", 2l);
    private Subscribable answer = answer("description", myQuestion, answerAuthor);
    
    private MockTemplateMailer templates = new MockTemplateMailer();
    private Mailer mailer = mock(Mailer.class);
    private Localization localization = mock(Localization.class);
    private Linker linker = mock(Linker.class);
    private NotificationMailer notificationMailer = new NotificationMailer(mailer, templates, localization, linker);

    @Test
    public void should_send_two_emails() throws Exception {
        ArrayList<SubscribableDTO> subscribablesDTO = new ArrayList<>();
        
        User otherSubscribed = user("other", "email@brutal.com", 1l);
        
        subscribablesDTO.add(new SubscribableDTO(answer, questionAuthor, myQuestion));
        subscribablesDTO.add(new SubscribableDTO(answer, otherSubscribed, myQuestion));
        
        notificationMailer.sendMails(subscribablesDTO);
        
        verify(mailer, times(2)).send(any(Email.class));
    }
    
    @Test
    public void should_send_one_email_with_three_subscribables() throws Exception {
        Subscribable comment = new Comment(answerAuthor, "comment");
        ArrayList<SubscribableDTO> subscribablesDTO = new ArrayList<>();
        subscribablesDTO.add(new SubscribableDTO(answer, questionAuthor, myQuestion));
        subscribablesDTO.add(new SubscribableDTO(comment, questionAuthor, myQuestion));
        subscribablesDTO.add(new SubscribableDTO(new Comment(answerAuthor, "blablabla"), questionAuthor, myQuestion));
        
        notificationMailer.sendMails(subscribablesDTO);
        
        verify(mailer, times(1)).send(any(Email.class));
    }
    
    
    

}
