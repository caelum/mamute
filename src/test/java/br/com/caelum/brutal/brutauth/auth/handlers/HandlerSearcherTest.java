package br.com.caelum.brutal.brutauth.auth.handlers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutal.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.vraptor.ioc.Container;

public class HandlerSearcherTest {

	private HandlerSearcher handlerSearcher;
	private AccessNotPermitedHandler defaultHandler;
	private SpecificHandler specificHandler;

	@Before
	public void setUp(){
		Container container = mock(Container.class);
		
		defaultHandler = new AccessNotPermitedHandler(null);
		when(container.instanceFor(AccessNotPermitedHandler.class)).thenReturn(defaultHandler);
		
		specificHandler = new SpecificHandler();
		when(container.instanceFor(SpecificHandler.class)).thenReturn(specificHandler);
		
		handlerSearcher = new HandlerSearcher(container);
	}
	
	@Test
	public void should_get_default_handler_if_class_doesnt_contains_annotation() {
		RuleWithoutSpecificHandlers rule = new RuleWithoutSpecificHandlers();
		assertEquals(defaultHandler, handlerSearcher.getHandler(rule));
	}
	
	@Test
	public void should_get_specific_handler() {
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		Annotation[] annotations = rule.getClass().getAnnotations();
		for (Annotation annotation : annotations) {
			System.out.println(annotation);
		}
		assertEquals(specificHandler, handlerSearcher.getHandler(rule));
	}

	public class RuleWithoutSpecificHandlers implements BrutauthRule{
	}
	
	@HandledBy(SpecificHandler.class)
	public class RuleWithSpecificHandlers implements BrutauthRule{
	}
	
	public class SpecificHandler implements RuleHandler{
		@Override
		public boolean handle(boolean isAllowed) {
			return false;
		}
	}
}

