package br.com.caelum.brutal.sanitizer;

import static br.com.caelum.brutal.sanitizer.QuotesSanitizer.sanitize;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QuotesSanitizerTest {
	
	@Test
	public void should_remove_quotes() throws Exception {
		String string = "My description = \"test\"; ";
		assertEquals("My description = test; ", sanitize(string));
	}
}
