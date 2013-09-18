package br.com.caelum.brutal.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.vraptor.proxy.InstanceCreator;
import br.com.caelum.vraptor.proxy.JavassistProxifier;
import br.com.caelum.vraptor.proxy.ReflectionInstanceCreator;

public class ContentProxifierTest extends TestCase {

	@Test
	public void should_sanitize_calls() {
		InstanceCreator creator = new ReflectionInstanceCreator();
		JavassistProxifier proxifier = new JavassistProxifier(creator);
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
