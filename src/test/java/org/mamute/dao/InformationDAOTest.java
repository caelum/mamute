package org.mamute.dao;

import static org.junit.Assert.assertEquals;
import static org.mamute.model.MarkedText.notMarked;
import static org.mamute.model.UpdateStatus.APPROVED;
import static org.mamute.model.UpdateStatus.NO_NEED_TO_APPROVE;
import static org.mamute.model.UpdateStatus.PENDING;
import static org.mamute.model.UpdateStatus.REFUSED;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.model.Answer;
import org.mamute.model.AnswerInformation;
import org.mamute.model.Information;
import org.mamute.model.LoggedUser;
import org.mamute.model.ModeratableAndPendingHistory;
import org.mamute.model.Question;
import org.mamute.model.QuestionInformation;
import org.mamute.model.QuestionInformationBuilder;
import org.mamute.model.Tag;
import org.mamute.model.UpdateStatus;
import org.mamute.model.User;
import org.mamute.model.interfaces.Moderatable;

public class InformationDAOTest extends DatabaseTestCase {

	private LoggedUser currentAuthor;
	private User author;
	private Tag java;
	private InformationDAO informations;
	private Question question;

	@Before
	public void setup(){
		author = user("namename", "email@gmail.com");
		currentAuthor = new LoggedUser(author, null);
		java = new Tag("java", "", null);
		session.save(author);
		session.save(java);
		informations = new InformationDAO(session);
		question = newQuestion(author);
	}
	
    @Test
    public void should_find_pending_answers_edits() {
        Answer answer = answer("info1 info1 info1 info1 info1 info1 info1 ", question, author);
        session.save(answer);
        newPendingChangesAnswer(answer, 2);
        
        ModeratableAndPendingHistory pendingByUpdatables = informations.pendingByUpdatables(Answer.class);
        
        List<Information> pendingInfoForAnswer = pendingByUpdatables.pendingInfoFor(answer);
        assertEquals(2, pendingInfoForAnswer.size());
    }

    @Test
    public void should_get_pending_history() {
        newQuestion(author);
        
        Question question2 = newQuestion(author);
        newChangesWithStatus(question2, 2, PENDING);
        
        Question question3 = newQuestion(author);
        newChangesWithStatus(question3, 3, PENDING);
        
        ModeratableAndPendingHistory pending = informations.pendingByUpdatables(Question.class);
        List<Moderatable> questions = pending.moderatables();
        
        assertEquals(2, questions.size());
        assertEquals(question2.getId(), questions.get(0).getId());
        List<Information> pendingQuestion2 = pending.pendingInfoFor(questions.get(0));
        assertEquals(2, pendingQuestion2.size());
        List<Information> pendingQuestion3 = pending.pendingInfoFor(questions.get(1));
        assertEquals(3, pendingQuestion3.size());
    }

    
    @Test
    public void should_get_pending_count() {
        Answer answer = answer("info1 info1 info1 info1 info1 info1 info1 ", question, author);
        session.save(answer);
        newPendingChangesAnswer(answer, 2);
        newChangesWithStatus(question, 3, PENDING);
        
        Long count = informations.pendingCount();
        
        assertEquals(5l, count.longValue());
    }

    @Test
    public void should_get_history_for_question_with_approved_editions() {
    	newChangesWithStatus(question, 2, APPROVED);
    	List<Information> approved = informations.historyForQuestion(question.getId());
    	
    	assertEquals(3, approved.size());
    }
    
    @Test
    public void should_get_history_for_question_with_pending_editions() {
    	newChangesWithStatus(question, 2, PENDING);
    	List<Information> approved = informations.historyForQuestion(question.getId());
    	
    	assertEquals(1, approved.size());
    }
    
    @Test
    public void should_get_history_for_question_with_auto_approved_editions() {
    	newChangesWithStatus(question, 2, NO_NEED_TO_APPROVE);
    	List<Information> approved = informations.historyForQuestion(question.getId());
    	
    	assertEquals(3, approved.size());
    }
    
    @Test
    public void should_get_history_for_question_with_refused_editions() {
    	newChangesWithStatus(question, 2, REFUSED);
    	List<Information> approved = informations.historyForQuestion(question.getId());
    	
    	assertEquals(1, approved.size());
    }
    
    private void newChangesWithStatus(Question question, int times, UpdateStatus status) {
    	for (int i = 0; i < times; i++) {
			QuestionInformation questionInfo = new QuestionInformationBuilder().withTag(java).with(author).build();
			question.enqueueChange(questionInfo, status);
		}
    }
	
	private void newPendingChangesAnswer(Answer answer, int times) {
		for (int i = 0; i < times; i++) {
	        AnswerInformation pendingInfo = new AnswerInformation(notMarked("info2 info2 info2 info2 info2 info2 info2 "), currentAuthor, answer, "new answer");
	        answer.enqueueChange(pendingInfo, PENDING);
		}
	}
	
	private Question newQuestion(User author){
		Question question = new QuestionBuilder().withAuthor(author).withTag(java).build();
		session.save(question);
		return question;
	}
	
}
