package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.controllers.QuestionController;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.vraptor.DefaultLinker;
import br.com.caelum.brutal.vraptor.Linker;

public class QuestionViewCounterTest extends TestCase {

	@Test
	public void should_increase_count_only_first_time() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getCookies()).thenReturn(new Cookie[]{});
		Linker linker = mock(DefaultLinker.class);
		QuestionController controller = mock(QuestionController.class);
		when(linker.linkTo(QuestionController.class)).thenReturn(controller);
		
		QuestionViewCounter questionViewCounter = new QuestionViewCounter(request, response, linker);
		Question question = new QuestionBuilder().withId(1l).build();
		
		questionViewCounter.ping(question);
		
		assertEquals(1l, question.getViews());
		
		Cookie cookie = new Cookie(QuestionViewCounter.COOKIE_PREFIX + question.getId(), "1");
		when(request.getCookies()).thenReturn(new Cookie[]{cookie});
		
		questionViewCounter.ping(question);
		assertEquals(1l, question.getViews());
	}

}
