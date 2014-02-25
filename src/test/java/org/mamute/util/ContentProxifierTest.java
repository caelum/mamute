package org.mamute.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.TestCase;
import org.mamute.model.Question;
import org.mamute.util.ContentProxifier;

import br.com.caelum.vraptor.proxy.JavassistProxifier;

public class ContentProxifierTest extends TestCase {

	@Test
	public void should_sanitize_calls() {
		JavassistProxifier proxifier = new JavassistProxifier();
		ContentProxifier contentProxifier = new ContentProxifier(proxifier);
		Question question = new QuestionBuilder()
			.withDescription("<input type='text' value='blablab' />blabla")
			.withTitle("<script>console.log('blabla')</script>not script")
			.build();
		Question proxied = contentProxifier.safeProxyFor(question, Question.class);
		assertEquals(proxied.getDescription(), "blabla");
		assertEquals(proxied.getTitle(), "not script");
	}

}
