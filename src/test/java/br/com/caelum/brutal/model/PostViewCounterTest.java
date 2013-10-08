package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.NewsBuilder;
import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.post.PostViewCounter;

public class PostViewCounterTest extends TestCase {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private PostViewCounter questionViewCounter;

	@Before
	public void setup() {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		questionViewCounter = new PostViewCounter(request, response);
		when(request.getCookies()).thenReturn(new Cookie[] {});
	}

	@Test
	public void should_increase_count_only_first_time() {
		Question question = new QuestionBuilder().withId(1l).build();

		questionViewCounter.ping(question);
		mockQuestionVisited(question);

		assertEquals(1l, question.getViews());

		questionViewCounter.ping(question);
		assertEquals(1l, question.getViews());
	}

	@Test
	public void should_increase_count_only_the_news_counter() {
		Question question = new QuestionBuilder().withId(1l).build();
		News news = new NewsBuilder().withId(1l).build();

		questionViewCounter.ping(question);
		mockQuestionVisited(question);

		questionViewCounter.ping(news);

		assertEquals(1l, question.getViews());
		assertEquals(1l, news.getViews());
	}

	private void mockQuestionVisited(Question question) {
		Cookie cookie = new Cookie(questionViewCounter.cookieKeyFor(question),
				"1");
		when(request.getCookies()).thenReturn(new Cookie[] { cookie });
	}
}
