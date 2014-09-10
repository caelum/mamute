package org.mamute.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mamute.model.MarkedText.notMarked;
import static org.mamute.model.UpdateStatus.PENDING;

import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.TestCase;
import org.mamute.model.Answer;
import org.mamute.model.AnswerInformation;
import org.mamute.model.Information;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;
import org.mamute.model.User;

public class AnswerTest extends TestCase {
    private User author = user("name", "email", 1l);
    private User editUser = user("edit", "editemail", 2l);
    private QuestionBuilder question = new QuestionBuilder();

	@Test
	public void should_mark_question_as_solved() {
		Question canILiveForever = question.build();
		Answer yes = answer("Yes", canILiveForever, null);
		
		assertEquals(null, canILiveForever.getSolution());
		
		yes.markAsSolution();
		
		assertEquals(yes, canILiveForever.getSolution());
	}
	
	@Test
	public void should_return_that_answer_is_solution_or_not() {
		Question canILiveForever = question.build();
		Answer yes = answer("Yes", canILiveForever, null);
		
		assertFalse(yes.isSolution());
		
		yes.markAsSolution();
		
		assertTrue(yes.isSolution());
	}
	
	
	@Test
	public void should_approve_answer_info() throws Exception {
		Question myQuestion = question.withTitle("question title").withDescription("description").withAuthor(author).build();
		Answer answer = answer("blablablab", myQuestion, author);
		
		Information approved = new AnswerInformation(notMarked("blablabalblab"), new LoggedUser(editUser, null), answer, "");
		answer.approve(approved);
		
		assertEquals(approved, answer.getInformation());
		assertEquals(editUser, answer.getLastTouchedBy());
	}
	
	@Test
	public void return_true_if_answer_has_pending_edits() throws Exception {
		Question myQuestion = question.withTitle("question title").withDescription("description").withAuthor(author).build();
		Answer answer = answer("blablablab", myQuestion, author);
		assertFalse(answer.hasPendingEdits());
		
		AnswerInformation approved = new AnswerInformation(notMarked("blablabalblab"), new LoggedUser(editUser, null), answer, "");
		answer.enqueueChange(approved, PENDING);
		assertTrue(answer.hasPendingEdits());
		
	}
	
}
