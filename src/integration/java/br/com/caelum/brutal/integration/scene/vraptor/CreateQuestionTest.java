package br.com.caelum.brutal.integration.scene.vraptor;

import org.junit.Test;

import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class CreateQuestionTest extends CustomVRaptorIntegration {

	@Test
	public void should_make_a_question() {
		String title = "My new question about java";
		String description = "just a question that i have about java hahahhaha";
		String tags = "java";
		UserFlow navigation = login(navigate(), karmaNigga().getEmail());
		navigation = createQuestionWithFlow(navigation, title, description, tags, false);
		VRaptorTestResult execute = navigation.followRedirect().execute();
	}

}
