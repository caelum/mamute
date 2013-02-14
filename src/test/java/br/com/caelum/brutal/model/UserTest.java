package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.interfaces.Updatable;

public class UserTest extends TestCase{
    
    private User author;
    private Updatable updateble;
    private User moderator;
    private User otherUser;

    @Before
    public void before_test() {
        author = new User("name", "email", "12234");
        author.setId(1l);
        Question question = question("title", "text", author);
        
        otherUser = new User("other", "email", "1234");
        otherUser.setId(2l);
        
        moderator = new User("yeah", "email", "1234").asModerator();
        moderator.setId(3l);
        
        
        updateble = question;
    }

    @Test
    public void author_should_be_authorized_to_update() {
        UpdateStatus status = author.canUpdate(updateble);
        assertEquals(UpdateStatus.NO_NEED_TO_APPROVE, status);
    }
    
    @Test
    public void non_author_update_should_be_pending() {
        UpdateStatus status = otherUser.canUpdate(updateble);
        assertEquals(UpdateStatus.PENDING, status);
    }
    
    @Test
    public void moderator_should_be_authorized_to_update() {
        UpdateStatus status = moderator.canUpdate(updateble);
        assertEquals(UpdateStatus.NO_NEED_TO_APPROVE, status);
    }
    
    @Test
    public void moderator_should_approve_question_information() throws Exception {
        Question question = question("question title", "question description", author);
        Information approvedInfo = new QuestionInformation("edited title", "edited desc", 
                new LoggedUser(otherUser, null), new ArrayList<Tag>(), "comment");
        
        moderator.approve(question, approvedInfo);
        
        assertEquals(approvedInfo, question.getInformation());
        assertTrue(question.getInformation().isModerated());
    }
    
    @Test
    public void moderator_should_approve_answer_information() throws Exception {
        Question question = question("question title", "question description", author);
        Answer answer = answer("answer description", question, author);
        AnswerInformation approvedInfo = answerInformation("new description", otherUser, answer);
        
        moderator.approve(answer, approvedInfo);
        
        assertEquals(approvedInfo, answer.getInformation());
        assertTrue(answer.getInformation().isModerated());
    }
    
}
