package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.dao.TestCase;

public class UpdaterTest extends TestCase{
    
    private User author;
    private Question question;

    @Before
    public void before_test() {
        author = new User("author", "author@gmail", "1234");
        author.setId(1l);
        question = question("titel", "description", author);
    }

    @Test
    public void should_pend_if_cant_update() {
        User user = new User("chico", "chico@gmail.com", "1234");
        user.setId(2l);
        
        Updater updater = new Updater();
        QuestionInformation newInformation = new QuestionInformation( "title", "description", new LoggedUser(user, null));
		UpdateStatus update = updater.update(question, newInformation);
        
        assertEquals(update, UpdateStatus.PENDING);
        assertTrue(question.getHistory().contains(newInformation));
    }
    
    @Test
    public void should_update_if_user_can_update() {
        UpdateStatus status = UpdateStatus.NO_NEED_TO_APPROVE;
        User authorized = mock(User.class);
        when(authorized.canUpdate(question)).thenReturn(status);
        
        Updater updater = new Updater();
        QuestionInformation newInformation = new QuestionInformation("new Title", "new description", new LoggedUser(authorized, null));
		UpdateStatus update = updater.update(question, newInformation);
        
        assertEquals(status, update);
        assertTrue(question.getHistory().contains(newInformation));
    }
    

}
