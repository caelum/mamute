package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.dao.TestCase;

public class UpdaterTest extends TestCase{
    
    private User author;
    private Question question;
    private Updater updater = new Updater();
    private Long id = 0l;

    @Before
    public void before_test() {
        author = user("author", "author@gmail", nextId());
        question = question("titel", "description", author);
    }

    @Test
    public void should_be_pending_if_is_not_author() {
        User user = user("chico", "chico@gmail.com", nextId());
        QuestionInformation newInformation = new QuestionInformation("title", "description", new LoggedUser(user, null));
        
		UpdateStatus update = updater.update(question, newInformation);
        
        assertEquals(UpdateStatus.PENDING, update);
        assertTrue(question.getHistory().contains(newInformation));
    }
    
    @Test
    public void should_update_if_is_moderator() {
        User moderator = user("moderator", "moderator@brutal", nextId()).asModerator();
        QuestionInformation newInformation = new QuestionInformation("title", "description", new LoggedUser(moderator, null));
        UpdateStatus update = updater.update(question, newInformation);
        
        assertEquals(UpdateStatus.NO_NEED_TO_APPROVE, update);
        assertTrue(question.getHistory().contains(newInformation));
    }
    
    @Test
    public void should_update_if_is_the_author() {
        QuestionInformation newInformation = new QuestionInformation("title", "description", new LoggedUser(author, null));
		UpdateStatus update = updater.update(question, newInformation);
        
        assertEquals(UpdateStatus.NO_NEED_TO_APPROVE, update);
        assertTrue(question.getHistory().contains(newInformation));
    }
    
    private Long nextId() {
        return id++;
    }

}
