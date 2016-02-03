package org.mamute.infra;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mamute.factory.MessageFactory;
import org.mamute.model.Answer;
import org.mamute.model.Comment;
import org.mamute.model.News;
import org.mamute.model.Question;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.vraptor.validator.I18nMessage;

@RunWith(MockitoJUnitRunner.class)
public class ModelUrlMappingTest {

	private static final String LOCALIZED_QUESTION_NAME = "(localized) Question";
	private static final String LOCALIZED_ANSWER_NAME = "(localized) Answer";
	private static final String LOCALIZED_COMMENT_NAME = "(localized) Comment";
	private static final String LOCALIZED_NEWS_NAME = "(localized) News";

	@Mock
	private MessageFactory messageFactory;

	private ModelUrlMapping modelUrlMapping;

	@Before
	public void setup() {
		mockLocalizedMessage("question.type_name", LOCALIZED_QUESTION_NAME);
		mockLocalizedMessage("answer.type_name", LOCALIZED_ANSWER_NAME);
		mockLocalizedMessage("comment.type_name", LOCALIZED_COMMENT_NAME);
		mockLocalizedMessage("news.type_name", LOCALIZED_NEWS_NAME);

		modelUrlMapping = new ModelUrlMapping(messageFactory);
	}

	private void mockLocalizedMessage(String key, String translation) {
		final I18nMessage message = Mockito.mock(I18nMessage.class);
		Mockito.when(message.getMessage()).thenReturn(translation);

		Mockito.when(messageFactory.build(Matchers.anyString(), Matchers.eq(key), Matchers.anyVararg())).thenReturn(message);
	}

	@Test
	public void should_return_question_for_localized_question_name() {
		assertEquals(Question.class, modelUrlMapping.getClassFor(LOCALIZED_QUESTION_NAME));
	}

	@Test
	public void should_return_question_for_question_simple_class_name() {
		assertEquals(Question.class, modelUrlMapping.getClassFor(Question.class.getSimpleName()));
	}

	@Test
	public void should_return_answer_for_localized_answer_name() {
		assertEquals(Answer.class, modelUrlMapping.getClassFor(LOCALIZED_ANSWER_NAME));
	}

	@Test
	public void should_return_answer_for_answer_simple_class_name() {
		assertEquals(Answer.class, modelUrlMapping.getClassFor(Answer.class.getSimpleName()));
	}

	@Test
	public void should_return_comment_for_localized_comment_name() {
		assertEquals(Comment.class, modelUrlMapping.getClassFor(LOCALIZED_COMMENT_NAME));
	}

	@Test
	public void should_return_comment_for_comment_simple_class_name() {
		assertEquals(Comment.class, modelUrlMapping.getClassFor(Comment.class.getSimpleName()));
	}

	@Test
	public void should_return_news_for_localized_news_name() {
		assertEquals(News.class, modelUrlMapping.getClassFor(LOCALIZED_NEWS_NAME));
	}

	@Test
	public void should_return_news_for_news_simple_class_name() {
		assertEquals(News.class, modelUrlMapping.getClassFor(News.class.getSimpleName()));
	}

	@Test(expected = NotFoundException.class)
	public void should_throw_not_found_exception() {
		modelUrlMapping.getClassFor("unknown-type");
	}
}
