package br.com.caelum.brutal.sanitizer;

import static br.com.caelum.brutal.sanitizer.HtmlSanitizer.sanitize;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HtmlSanitizerTest {

	
	@Test
	public void shouldEscapeLTGTWhenPossible() {
		String html = "< bla >";
		String expected = "&lt;bla&gt;";
		String sanitized = sanitize(html);
		assertEquals(expected, sanitized);
	}
	
	@Test
	public void shouldAddNoFollowIntoLinks() {
		String html = "<a href=\"http://www.teste.com.br\">teste</a>";
		String expected = "<a href=\"http://www.teste.com.br\" rel=\"nofollow\">teste</a>";
		String sanitized = sanitize(html);
		assertEquals(expected, sanitized);
	}
	


	@Test
	public void shouldRemoveLinksWithInvalidProtocol() {
		String html = "<a href=\"ftp://www.teste.com.br\">teste</a>";
		String expected = "teste";
		String sanitized = sanitize(html);
		assertEquals(expected, sanitized);
	}
	
	@Test
	public void shouldRemoveTagScript() {
		String html = "<script>function deleteAll(){document.getElementsByTagName(\"body\")[0].remove()}</script>";
		String sanitized = sanitize(html);
		assertTrue(sanitized.isEmpty());
	}
	
	@Test
	public void shouldRemoveInvalidAttributesOfALink() {
		String html = "<a class=\"my-class\" href=\"http://www.teste.com.br\">teste</a>";
		String expected = "<a href=\"http://www.teste.com.br\" rel=\"nofollow\">teste</a>";
		String sanitized = sanitize(html);
		assertEquals(expected, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagP() {
		String html = "<p>My text maroto</p>";
		String sanitized = sanitize(html);
		assertEquals(html, sanitized);
	}

	@Test
	public void shouldNotRemoveTagPre() {
		String html = "<pre>My code maroto</pre>";
		String sanitized = sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagCode() {
		String html = "<code>My inline code maroto</code>";
		String sanitized = sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagImg() {
		String html = "<img src=\"myimage.png\" alt=\"chrome\" />";
		String sanitized = sanitize(html);
		assertEquals(html, sanitized);
	}
	

	@Test
	public void shouldAllowImagesSize() {
		String html = "<img src=\"http://www.teste.com.br\" alt=\"x\" width=\"5\" height=\"3\" />\n";
		String sanitized = sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotAllowOtherAttributesForImages() {
		String html = "<img src=\"http://www.teste.com.br\" alt=\"x\" width=\"5\" height=\"3\" somethingElse=\"foo\" />";
		String expected = "<img src=\"http://www.teste.com.br\" alt=\"x\" width=\"5\" height=\"3\" />";
		String sanitized = sanitize(html);
		assertEquals(expected, sanitized);
	}
	
	
	
	@Test
	public void shouldNotRemoveTagKbd() {
		String html = "<kbd>shift</kbd>";
		String sanitized = sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagOl() {
		String html = "<ol><li>shift</li><li>ctrl</li></ol>";
		String sanitized = sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagUl() {
		String html = "<ul><li>shift</li><li>ctrl</li></ul>";
		String sanitized = sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagStrong() {
		String html = "<strong>leo</strong>";
		String sanitized = sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagH2() {
		String html = "<h2>Title</h2>";
		String sanitized = sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagBlockquote() {
		String html = "<blockquote>My quotation</blockquote>";
		String sanitized = sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagHr() {
		String html = "<hr />";
		String sanitized = sanitize(html);
		assertEquals(html, sanitized);
	}


}
