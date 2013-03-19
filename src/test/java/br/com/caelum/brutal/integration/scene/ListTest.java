package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.integration.pages.Home.HomeQuestion;

public class ListTest extends AcceptanceTestBase{

	@Test
	public void should_list_unsolved_questions() {
		boolean hasOnlyUnsolvedQuestions = 
				home()
				.toUnsolvedList()
				.hasOnlyUnsolvedQuestions();
		assertTrue(hasOnlyUnsolvedQuestions);
	}

	@Test
	public void should_list_by_tag() {
		String tag = "java";
		boolean hasOnlyQuestionsWithTheTag = 
				home()
				.toWithTagList(tag)
				.hasOnlyQuestionsWithTag(tag);
		assertTrue(hasOnlyQuestionsWithTheTag);
	}
	
	@Test
	public void should_not_show_questions_with_too_low_reputation() throws Exception {
		List<HomeQuestion> homeQuestions = home().allQuestions();
		for (HomeQuestion homeQuestion : homeQuestions) {
			assertTrue(homeQuestion.getVoteCount() > QuestionDAO.SPAM_BOUNDARY);
		}
	}

}
