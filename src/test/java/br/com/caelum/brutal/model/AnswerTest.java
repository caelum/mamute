package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.brutal.dao.TestCase;

public class AnswerTest extends TestCase {
    private User author = user("name", "email", 1l);
    private User editUser = user("edit", "editemail", 2l);
    private User moderator = user("moderator", "moderatoremail", 3l).asModerator();

	@Test
	public void should_mark_question_as_solved() {
		Question canILiveForever = question("", "", null);
		Answer yes = answer("Yes", canILiveForever, null);
		
		assertEquals(null, canILiveForever.getSolution());
		
		yes.markAsSolution();
		
		assertEquals(yes, canILiveForever.getSolution());
	}
	
	@Test
	public void should_return_that_answer_is_solution_or_not() {
		Question canILiveForever = question("", "", null);
		Answer yes = answer("Yes", canILiveForever, null);
		
		assertFalse(yes.isSolution());
		
		yes.markAsSolution();
		
		assertTrue(yes.isSolution());
	}
	
	@Test
    public void should_approve_answer_info() throws Exception {
        Question question = question("question title", "description", author);
        Answer answer = answer("blablablab", question, author);
        
        Information approved = new AnswerInformation("blablabalblab", new LoggedUser(editUser, null), answer, "");
        answer.approve(approved, moderator);
        
        assertEquals(approved, answer.getInformation());
        assertEquals(editUser, answer.getLastTouchedBy());
    }
	
	@Test
	public void should_not_update_approve_from_non_moderator() throws Exception {
	    Question question = question("question title", "description", author);
	    Answer answer = answer("blablablab", question, author);
	    AnswerInformation original = answer.getInformation();
	    User nonModerator = user("nonmoderator", "nonmoderator", 4l);
	    
	    Information approved = new AnswerInformation("blablabalblab", new LoggedUser(editUser, null), answer, "");
        answer.approve(approved, nonModerator);
        
	    assertEquals(original, answer.getInformation());
	    assertEquals(author, answer.getLastTouchedBy());
	}
}
