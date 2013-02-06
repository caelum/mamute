package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integracao.dao.TestCase;

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
    
}
