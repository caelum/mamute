package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.controllers.Updater;
import br.com.caelum.brutal.dao.HistoryDAO;

public class UpdaterTest {
    
    private HistoryDAO edits;
    private User author;
    private Question question;

    @Before
    public void before_test() {
        edits = mock(HistoryDAO.class);
        author = new User("author", "author@gmail", "1234");
        author.setId(1l);
        question = new Question("titel", "description");
        question.setAuthor(author);
    }

    @Test
    public void should_refuse_if_cant_update() {
        User user = new User("chico", "chico@gmail.com", "1234");
        user.setId(2l);
        
        Updater updater = new Updater();
        UpdateStatus update = updater.update(question, new QuestionInformation( "title", "description", user));
        
        assertEquals(update, UpdateStatus.REFUSED);
        verify(edits, never()).save(Mockito.any(UpdateHistory.class));
    }
    
    @Test
    public void should_refuse_if_is_invalid_field() {
        Updater updater = new Updater();
        UpdateStatus update = updater.update(question, new QuestionInformation("new title", "new description", author));
        
        assertEquals(update, UpdateStatus.REFUSED);
        verify(edits, never()).save(Mockito.any(UpdateHistory.class));
    }
    
    @Test
    public void should_update_if_user_can_update() {
        UpdateStatus status = UpdateStatus.NO_NEED_TO_APPROVE;
        User authorized = mock(User.class);
        when(authorized.canUpdate(question)).thenReturn(status);
        
        Updater updater = new Updater();
        UpdateStatus update = updater.update(question, new QuestionInformation("new Title", "new description", authorized));
        
        assertEquals(update, status);
        verify(edits, times(1)).save(Mockito.any(UpdateHistory.class));
    }
    

}
