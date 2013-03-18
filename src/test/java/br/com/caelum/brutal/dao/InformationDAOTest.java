package br.com.caelum.brutal.dao;

import static br.com.caelum.brutal.model.UpdateStatus.PENDING;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.QuestionInformationBuilder;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.UpdatablesAndPendingHistory;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;

public class InformationDAOTest extends DatabaseTestCase {

	private LoggedUser currentAuthor;
	private User author;
	private Tag java;

	@Before
	public void setup(){
		author = user("namename", "email@gmail.com");
		currentAuthor = new LoggedUser(author, null);
		java = new Tag("java", "", null);
		session.save(author);
		session.save(java);
	}
	
    @Test
    public void should_find_pending_answers_edits() {
        InformationDAO informations = new InformationDAO(session);
        
        Question question = newQuestion(author);
        
        Answer answer = answer("info1 info1 info1 info1 info1 info1 info1 ", question, author);
        session.save(answer);
        newPendingChanges(answer, 2);
        
        UpdatablesAndPendingHistory pendingByUpdatables = informations.pendingByUpdatables(Answer.class);
        
        List<Information> pendingInfoForAnswer = pendingByUpdatables.pendingInfoFor(answer);
        assertEquals(2, pendingInfoForAnswer.size());
    }

    @Test
    public void should_get_pending_history() {
        InformationDAO informations = new InformationDAO(session);

        newQuestion(author);
        
        Question question2 = newQuestion(author);
        newPendingChanges(question2, 2);
        
        Question question3 = newQuestion(author);
        newPendingChanges(question3, 3);
        
        UpdatablesAndPendingHistory pending = informations.pendingByUpdatables(Question.class);
        List<Moderatable> questions = pending.moderatables();
        
        assertEquals(2, questions.size());
        assertEquals(question2.getId(), questions.get(0).getId());
        List<Information> pendingQuestion2 = pending.pendingInfoFor(questions.get(0));
        assertEquals(2, pendingQuestion2.size());
        List<Information> pendingQuestion3 = pending.pendingInfoFor(questions.get(1));
        assertEquals(3, pendingQuestion3.size());
    }


	private void newPendingChanges(Question question, int times) {
		for (int i = 0; i < times; i++) {
			QuestionInformation pendingInfo = new QuestionInformationBuilder().withTag(java).with(author).build();
			question.enqueueChange(pendingInfo, PENDING);
		}
	}
	
	private void newPendingChanges(Answer answer, int times) {
		for (int i = 0; i < times; i++) {
	        AnswerInformation pendingInfo = new AnswerInformation("info2 info2 info2 info2 info2 info2 info2 ", currentAuthor, answer, "new answer");
	        answer.enqueueChange(pendingInfo, PENDING);
		}
	}
	
	private Question newQuestion(User author){
		Question question = new QuestionBuilder().withAuthor(author).withTag(java).build();
		session.save(question);
		return question;
	}
	
}
