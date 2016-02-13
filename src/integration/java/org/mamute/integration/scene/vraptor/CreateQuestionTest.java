package org.mamute.integration.scene.vraptor;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mamute.model.Question;
import org.mamute.model.Tag;

import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class CreateQuestionTest extends CustomVRaptorIntegration {

	@Test
	public void should_make_a_question() {
		String title = "My new question about java";
		String description = "just a question that i have about java hahahhaha";
		Tag tag = tag("java");

		UserFlow navigation = login(navigate(), karmaNigga().getEmail());
		navigation = createQuestionWithFlow(navigation, title, description, tag.getName(), false);
		VRaptorTestResult createdQuestion = navigation.followRedirect().execute();
		createdQuestion.wasStatus(200).isValid();

		Question question = createdQuestion.getObject("question");
		Tag questionTag = question.getTags().get(0);

		assertThat(question.getTitle(), equalTo(title));
		assertThat(question.getDescription(), equalTo(description));
		assertThat(questionTag.getName(), equalTo(tag.getName()));
	}

}
