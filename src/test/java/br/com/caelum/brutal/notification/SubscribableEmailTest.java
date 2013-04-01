package br.com.caelum.brutal.notification;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.SubscribableDTO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Subscribable;

public class SubscribableEmailTest extends TestCase {
    private QuestionBuilder question = new QuestionBuilder();
    private User questionAuthor = user("chico", "chico@gmail.com", 1l);
    private Question myQuestion = question.withTitle("question title").withDescription("question description").withAuthor(questionAuthor).build();
    private User answerAuthor = user("answer user", "answer@brutal.com", 2l);
    private Subscribable answer = answer("description", myQuestion, answerAuthor);

	@Test
	public void should_group_by_user_email() throws Exception {
	    ArrayList<SubscribableDTO> subscribablesDTO = new ArrayList<>();
	    
	    User otherSubscribed = user("other", "email@brutal.com", 1l);

	    subscribablesDTO.add(new SubscribableDTO(answer, questionAuthor, myQuestion));
	    subscribablesDTO.add(new SubscribableDTO(answer, questionAuthor, myQuestion));
	    subscribablesDTO.add(new SubscribableDTO(answer, otherSubscribed, myQuestion));
	    
	    List<SubscribableEmail> subscribableEmails = SubscribableEmail.buildSubscribableEmails(subscribablesDTO);
	    assertEquals(2, subscribableEmails.size());
	    assertEquals(2, subscribableEmails.get(0).getSubscribables().size());
	    assertEquals(1, subscribableEmails.get(1).getSubscribables().size());
	}
}
