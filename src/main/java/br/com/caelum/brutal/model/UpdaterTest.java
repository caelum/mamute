package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.caelum.brutal.controllers.Updater;
import br.com.caelum.brutal.dao.EditDAO;

public class UpdaterTest {
    
    private EditDAO edits;
    private User author;
    private Question question;

    @Before
    public void before_test() {
        edits = mock(EditDAO.class);
        author = new User("author", "author@gmail", "1234");
        author.setId(1l);
        question = new Question("titel", "description");
        question.setAuthor(author);
    }

    @Test
    public void should_refuse_if_cant_update() {
        User user = new User("chico", "chico@gmail.com", "1234");
        user.setId(2l);
        
        Updater updater = new Updater(user, edits);
        UpdateStatus update = updater.update(question, "description", "new desc");
        
        assertEquals(update, UpdateStatus.REFUSED);
        verify(edits, never()).save(Mockito.any(UpdateHistory.class));
    }
    
    @Test
    public void should_refuse_if_is_invalid_field() {
        Updater updater = new Updater(author, edits);
        UpdateStatus update = updater.update(question, "id", "new desc");
        
        assertEquals(update, UpdateStatus.REFUSED);
        verify(edits, never()).save(Mockito.any(UpdateHistory.class));
    }
    
    @Test
    public void should_update_if_user_can_update() {
        UpdateStatus status = UpdateStatus.NO_NEED_TO_APPROVE;
        User authorized = mock(User.class);
        when(authorized.canUpdate(question)).thenReturn(status);
        
        Updater updater = new Updater(authorized, edits);
        UpdateStatus update = updater.update(question, "description", "new desc");
        
        assertEquals(update, status);
        verify(edits, times(1)).save(Mockito.any(UpdateHistory.class));
    }
    

}
