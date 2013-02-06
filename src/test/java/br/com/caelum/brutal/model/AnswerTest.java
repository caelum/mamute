package br.com.caelum.brutal.model;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.caelum.brutal.integracao.dao.TestCase;

public class AnswerTest  extends TestCase{

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

}
