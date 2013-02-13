package br.com.caelum.brutal.integration.scene;

import static junit.framework.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QuestionTest extends AcceptanceTestBase{
	
	@Before
	public void set_up(){
		home().toLoginPage().login("leonardo.wolter@caelum.com.br", "123456");
	}
	
	@After
	public void tear_down(){
		home().logOut();
	}
	
	@Test
	public void should_make_a_question(){
		String questionTitle = "My new question about java";
		boolean isTheQuestion = home().toNewQuestionPage()
			.newQuestion(questionTitle, "just a question that i have about java hahahhaha", "java")
			.isTheQuestion(questionTitle);
		assertTrue(isTheQuestion);
	}

}
