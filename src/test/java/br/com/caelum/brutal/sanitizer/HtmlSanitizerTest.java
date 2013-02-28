package br.com.caelum.brutal.sanitizer;

import static br.com.caelum.brutal.sanitizer.HtmlSanitizer.sanitize;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class HtmlSanitizerTest {

	@Test
	public void shouldAddNoFollowIntoLinks() {
		String html = "<a href=\"http://www.teste.com.br\">teste</a>";
		String expected = "<a href=\"http://www.teste.com.br\" rel=\"nofollow\">teste</a>";
		String sanitized = sanitize(html);
		assertEquals(expected, sanitized);
	}
	

	@Test
	public void shouldAllowImages() {
		String html = "<img src=\"http://www.teste.com.br\" alt=\"x\" width=\"5\" height=\"3\">";
		String expected = "<img src=\"http://www.teste.com.br\" alt=\"x\" width=\"5\" height=\"3\">";
		String sanitized = sanitize(html);
		assertEquals(expected, sanitized);
	}

}
