package org.mamute.sanitizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ATTRIBUTES_KEY_PREFIX;
import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ATTRIBUTES_WHITELIST_KEY_SUFIX;
import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ELEMENTS_KEY;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.vraptor.environment.Environment;

@RunWith(MockitoJUnitRunner.class)
public class HtmlSanitizerTest {

	@Mock private Environment env;
	private HtmlSanitizer htmlSanitizer;
	
	@Before
	public void setUp(){
		Mockito.when(env.get(ALLOWED_ELEMENTS_KEY)).thenReturn("a, blockquote, code, em, h1, h2, hr, img, kbd, li, ol, p, pre, strong, ul");
		Mockito.when(env.get(ALLOWED_ATTRIBUTES_KEY_PREFIX+"a")).thenReturn("href");
		Mockito.when(env.get(ALLOWED_ATTRIBUTES_KEY_PREFIX+"pre")).thenReturn("class");
		Mockito.when(env.get(ALLOWED_ATTRIBUTES_KEY_PREFIX+"img")).thenReturn("src, alt, width, height");
		Mockito.when(env.get(ALLOWED_ATTRIBUTES_KEY_PREFIX+"iframe")).thenReturn("href");
		Mockito.when(env.get(ALLOWED_ATTRIBUTES_KEY_PREFIX+"iframe"+ALLOWED_ATTRIBUTES_WHITELIST_KEY_SUFIX+"href")).thenReturn(".*soundcloud.com\\/tracks\\/.*|.*youtube.com\\/embed\\/.*|.*//player.vimeo.com\\/video\\/.*");
		htmlSanitizer = new HtmlSanitizer(env);
		htmlSanitizer.setUp();
	}
	
	
	@Test 
	public void should_escape_LTGT_wehen_possible() {
		String html = "< bla >";
		String expected = "&lt; bla &gt;";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(expected, sanitized);
	}
	
	@Test
	public void should_remove_textarea() {
		String html = "<textarea>";
		String expected = "";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(expected, sanitized);
	}
	
	@Test
	public void should_remove_javascript_from_href() {
		String html = "<a href=\"javascript:\">";
		String expected = "";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(expected, sanitized);
	}
	
	
	@Test
	public void shouldAddNoFollowIntoLinks() {
		String html = "<a href=\"http://www.teste.com.br\">teste</a>";
		String expected = "<a href=\"http://www.teste.com.br\" rel=\"nofollow\">teste</a>";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(expected, sanitized);
	}

	@Test
	public void shouldRemoveLinksWithInvalidProtocol() {
		String html = "<a href=\"ftp://www.teste.com.br\">teste</a>";
		String expected = "teste";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(expected, sanitized);
	}
	
	@Test
	public void shouldRemoveTagScript() {
		String html = "<script>function deleteAll(){document.getElementsByTagName(\"body\")[0].remove()}</script>";
		String sanitized = htmlSanitizer.sanitize(html);
		assertTrue(sanitized.isEmpty());
	}
	
	@Test
	public void shouldRemoveInvalidAttributesOfALink() {
		String html = "<a class=\"my-class\" href=\"http://www.teste.com.br\">teste</a>";
		String expected = "<a href=\"http://www.teste.com.br\" rel=\"nofollow\">teste</a>";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(expected, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagP() {
		String html = "<p>My text maroto</p>";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(html, sanitized);
	}

	@Test
	public void shouldNotRemoveTagPre() {
		String html = "<pre>My code maroto</pre>";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagCode() {
		String html = "<code>My inline code maroto</code>";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagImg() {
		String html = "<img src=\"myimage.png\" alt=\"chrome\" />";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(html, sanitized);
	}
	

	@Test
	public void shouldAllowImagesSize() {
		String html = "<img src=\"http://www.teste.com.br\" alt=\"x\" width=\"5\" height=\"3\" />\n";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotAllowOtherAttributesForImages() {
		String html = "<img src=\"http://www.teste.com.br\" alt=\"x\" width=\"5\" height=\"3\" somethingElse=\"foo\" />";
		String expected = "<img src=\"http://www.teste.com.br\" alt=\"x\" width=\"5\" height=\"3\" />";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(expected, sanitized);
	}
	
	
	
	@Test
	public void shouldNotRemoveTagKbd() {
		String html = "<kbd>shift</kbd>";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagOl() {
		String html = "<ol><li>shift</li><li>ctrl</li></ol>";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagUl() {
		String html = "<ul><li>shift</li><li>ctrl</li></ul>";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagStrong() {
		String html = "<strong>leo</strong>";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagH2() {
		String html = "<h2>Title</h2>";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagBlockquote() {
		String html = "<blockquote>My quotation</blockquote>";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagHr() {
		String html = "<hr />";
		String sanitized = htmlSanitizer.sanitize(html);
		assertEquals(html, sanitized);
	}


}
