package org.mamute.integration.scene.vraptor;

import static br.com.caelum.vraptor.test.http.Parameters.initWith;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.mamute.dao.QuestionDAO;
import org.mamute.model.Question;

import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class ListTest extends CustomVRaptorIntegration{
	
	@Test
	public void should_list_unsolved_questions() {
		UserFlow navigation = unsolvedQuestions();
		VRaptorTestResult unsolved = navigation.execute();
		unsolved.wasStatus(200).isValid();
		
		List<Question> questions = unsolved.getObject("questions");
		
		boolean onlyUnsolved = true;
		for (Question q : questions) {
			if(q.getAnswersCount() != 0) {
				onlyUnsolved = false;
				break;
			}
		}
		assertTrue(onlyUnsolved);
	}
	
	@Test
	public void should_list_by_tag() {
		String tag = "java";
		UserFlow navigation = tagList(tag);
		VRaptorTestResult tagList = navigation.execute();
		tagList.wasStatus(200).isValid();
		List<Question> questions = tagList.getObject("questions");
		
		boolean onlyTag = true;
		for (Question q : questions) {
			if(!q.getTagsAsString(" ").contains(tag)) {
				onlyTag = false;
				break;
			}
		}
		assertTrue(onlyTag);
	}

	@Test
	public void should_not_show_questions_with_too_low_reputation() {
		UserFlow navigation = home();
		VRaptorTestResult home = navigation.execute();
		home.wasStatus(200).isInvalid();
		List<Question> questions = home.getObject("questions");
		boolean showLowReputation = false;
		for (Question q : questions) {
			if (q.getVoteCount() <= QuestionDAO.SPAM_BOUNDARY) {
				showLowReputation = true;
				break;
			}
		}
		assertFalse(showLowReputation);
	}

	private UserFlow home() {
		return navigate().get("/", initWith("p", 0));
	}

	private UserFlow tagList(String tag) {
		return navigate().get(("/tag/" + tag), initWith("tagName", tag).add("p", 0).add("semRespostas", false));
	}

	private UserFlow unsolvedQuestions() {
		return navigate().get("/sem-respostas", initWith("p", 0));
	}
}
