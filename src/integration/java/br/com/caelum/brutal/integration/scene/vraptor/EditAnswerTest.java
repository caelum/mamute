package br.com.caelum.brutal.integration.scene.vraptor;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import br.com.caelum.brutal.integration.pages.QuestionPage;
import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.http.Parameters;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class EditAnswerTest extends CustomVRaptorIntegration {

	@Test
	public void should_edit_answer_of_other_author() throws Exception {
		UserFlow navigation = login(navigate(), "moderator@caelum.com.br", "123456");
		navigation = createQuestion(navigation, "Titulo da questao hahaha", "Descricao da questao longa demais", "java", false);
		
		VRaptorTestResult questionCreated = navigation.followRedirect().execute();
		questionCreated.wasStatus(200);
		System.out.println(questionCreated.getLastPath());
		
		
		
//    	loginAsModerator();
//    	home().toNewQuestionPage().newQuestion("question question question question", 
//    			"description description description description description", "java")
//    			.answer("answer answer answer answer answer answer answer answer");
//    	logout();
//    	
//        loginWithALotOfKarma();
//        
//        QuestionPage questionPage = home().toFirstQuestionPage()
//            .toEditFirstAnswerPage()
//            .edit("new answer with more than 30 characters aw  yeah !!!", 
//                "any comment");
//        
//        boolean containsConfirmationMessage = questionPage
//            .confirmationMessages()
//            .contains(message("status.pending"));
//        
//        assertTrue(containsConfirmationMessage);

	}

	private UserFlow firstQuestionFromList(UserFlow navigation) {
		navigation.get("/");
		return navigation;
	}

	@Test
	@Ignore
	public void simple() throws Exception {
		VRaptorTestResult result = navigate()
				.post("/login",
						Parameters.initWith("email", "moderator@caelum.com.br")
						.add("password", "123456")
				).followRedirect().execute();
		System.out.println("** "+result.getResponseBody());
//		VRaptorTestResult result = navigate()
//				.get("/").followRedirect().execute();
//		System.out.println("** "+result.getResponseBody());
//		System.out.println(result.getLastPath());
	}
	
	@AfterClass
	public static void tearDownClass() throws IOException {
		FileUtils.deleteDirectory(new File("jsp-compilation/"));
	}
	
}
