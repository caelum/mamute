package org.mamute.sanitizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ATTRIBUTES_KEY_PREFIX;
import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ATTRIBUTES_WHITELIST_KEY_SUFIX;
import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ELEMENTS_KEY;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.html.PolicyFactory;

import br.com.caelum.vraptor.environment.Environment;

@RunWith(MockitoJUnitRunner.class)
public class HtmlSanitizerTest {

	@Mock private Environment env;
	private HtmlSanitizer htmlSanitizer;
	
	@Before
	public void setUp(){
		envReturns(ALLOWED_ELEMENTS_KEY, "a, blockquote, code, em, h1, h2, hr, img, kbd, li, ol, p, pre, strong, ul, iframe");
		envReturns(ALLOWED_ATTRIBUTES_KEY_PREFIX+"a", "href");
		envReturns(ALLOWED_ATTRIBUTES_KEY_PREFIX+"pre", "class");
		envReturns(ALLOWED_ATTRIBUTES_KEY_PREFIX+"img", "src, alt, width, height");
		envReturns(ALLOWED_ATTRIBUTES_KEY_PREFIX+"iframe", "src, width, height, scrolling, frameborder");
		envReturns(ALLOWED_ATTRIBUTES_KEY_PREFIX+"iframe"+ALLOWED_ATTRIBUTES_WHITELIST_KEY_SUFIX+"href", ".*soundcloud.com\\/tracks\\/.*|.*youtube.com\\/embed\\/.*|.*//player.vimeo.com\\/video\\/.*");

		final HtmlElementsBuilder htmlElementsBuilder = new HtmlElementsBuilder(env, new HtmlAttributesBuilder(env));
		htmlElementsBuilder.setUp();
		MamutePolicyProducer mamutePolicyProducer = new MamutePolicyProducer(htmlElementsBuilder);
		mamutePolicyProducer.setUp();
		PolicyFactory policy = mamutePolicyProducer.getInstance();

		htmlSanitizer = new HtmlSanitizer(policy);
	}

	@Test 
	public void should_escape_LTGT_wehen_possible() {
		String html = "< bla >";
		String expected = "&lt; bla &gt;";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(expected, sanitized);
	}
	
	@Test
	public void should_remove_textarea() {
		String html = "<textarea>";
		String expected = "";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(expected, sanitized);
	}
	
	@Test
	public void should_remove_javascript_from_href() {
		String html = "<a href=\"javascript:\">";
		String expected = "";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(expected, sanitized);
	}
	
	
	@Test
	public void shouldAddNoFollowIntoLinks() {
		String html = "<a href=\"http://www.teste.com.br\">teste</a>";
		String expected = "<a href=\"http://www.teste.com.br\" target=\"_blank\" rel=\"nofollow\">teste</a>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(expected, sanitized);
	}

	@Test
	public void shouldAddTargetBlankIntoLinks() {
		String html = "<a href=\"http://www.teste.com.br\" rel=\"nofollow\">teste</a>";
		String expected = "<a href=\"http://www.teste.com.br\" target=\"_blank\" rel=\"nofollow\">teste</a>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(expected, sanitized);
	}

	@Test
	public void shouldChangeTargetToBlankInLinks() {
		String html = "<a href=\"http://www.teste.com.br\" rel=\"nofollow\" target=\"_self\">teste</a>";
		String expected = "<a href=\"http://www.teste.com.br\" target=\"_blank\" rel=\"nofollow\">teste</a>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(expected, sanitized);
	}

	@Test
	public void shouldRemoveLinksWithInvalidProtocol() {
		String html = "<a href=\"ftp://www.teste.com.br\">teste</a>";
		String expected = "teste";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(expected, sanitized);
	}
	
	@Test
	public void shouldRemoveTagScript() {
		String html = "<script>function deleteAll(){document.getElementsByTagName(\"body\")[0].remove()}</script>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertTrue(sanitized.isEmpty());
	}
	
	@Test
	public void shouldRemoveInvalidAttributesOfALink() {
		String html = "<a class=\"my-class\" href=\"http://www.teste.com.br\" target=\"_blank\">teste</a>";
		String expected = "<a href=\"http://www.teste.com.br\" target=\"_blank\" rel=\"nofollow\">teste</a>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(expected, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagP() {
		String html = "<p>My text maroto</p>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}

	@Test
	public void shouldNotRemoveTagPre() {
		String html = "<pre>My code maroto</pre>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagCode() {
		String html = "<code>My inline code maroto</code>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagImg() {
		String html = "<img src=\"myimage.png\" alt=\"chrome\" />";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}
	

	@Test
	public void shouldAllowImagesSize() {
		String html = "<img src=\"http://www.teste.com.br\" alt=\"x\" width=\"5\" height=\"3\" />\n";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotAllowOtherAttributesForImages() {
		String html = "<img src=\"http://www.teste.com.br\" alt=\"x\" width=\"5\" height=\"3\" somethingElse=\"foo\" />";
		String expected = "<img src=\"http://www.teste.com.br\" alt=\"x\" width=\"5\" height=\"3\" />";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(expected, sanitized);
	}
	
	
	
	@Test
	public void shouldNotRemoveTagKbd() {
		String html = "<kbd>shift</kbd>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagOl() {
		String html = "<ol><li>shift</li><li>ctrl</li></ol>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagUl() {
		String html = "<ul><li>shift</li><li>ctrl</li></ul>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagStrong() {
		String html = "<strong>leo</strong>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagH2() {
		String html = "<h2>Title</h2>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagBlockquote() {
		String html = "<blockquote>My quotation</blockquote>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}
	
	@Test
	public void shouldNotRemoveTagHr() {
		String html = "<hr />";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}
	
	@Test
	public void should_not_remove_tag_iframe_from_sound_cloud() {
		String html = "<ol><li><iframe width=\"100%\" height=\"166\" scrolling=\"no\" frameborder=\"no\" src=\"https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/tracks/119929315\"></iframe></li><li>ctrl</li></ol>";
		String htmlSanitized = "<ol><li><iframe width=\"100%\" height=\"166\" scrolling=\"no\" frameborder=\"no\" src=\"https://w.soundcloud.com/player/?url&#61;https%3A//api.soundcloud.com/tracks/119929315\"></iframe></li><li>ctrl</li></ol>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(htmlSanitized, sanitized);
	}
	
	@Test
	public void should_not_remove_tag_iframe_from_youtube() {
		String html = "<ol><li><iframe width=\"560\" height=\"315\" src=\"//www.youtube.com/embed/d9geBjThzwI\" frameborder=\"0\" allowfullscreen></iframe></iframe></li><li>ctrl</li></ol>";
		String htmlSanitized = "<ol><li><iframe width=\"560\" height=\"315\" src=\"//www.youtube.com/embed/d9geBjThzwI\" frameborder=\"0\"></iframe></li><li>ctrl</li></ol>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(htmlSanitized, sanitized);
	}
	
	@Test
	public void should_not_remove_tag_iframe_from_vimeo() {
		String html = "<ol><li><iframe width=\"560\" height=\"315\" src=\"//player.vimeo.com/video/87484369\" frameborder=\"0\" allowfullscreen></iframe></iframe></li><li>ctrl</li></ol>";
		String htmlSanitized = "<ol><li><iframe width=\"560\" height=\"315\" src=\"//player.vimeo.com/video/87484369\" frameborder=\"0\"></iframe></li><li>ctrl</li></ol>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(htmlSanitized, sanitized);
	}
	
	@Test
	public void should_remove_tag_iframe_not_from_sound_cloud() {
		String html = "<ol><li><iframe width=\"100%\" height=\"166\" scrolling=\"no\" frameborder=\"no\" <script>alert('bla');</script> src=\"https://w.youtube.com/player/?url=https%3A//api.youtube.com/tracks/119929315\"></iframe></li><li>ctrl</li></ol>";
		String htmlSanitized = "<ol><li><iframe width=\"100%\" height=\"166\" scrolling=\"no\" frameborder=\"no\"></iframe></li><li>ctrl</li></ol>";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(htmlSanitized, sanitized);
	}
	
	@Test
	public void should_return_empty_sanitized_text_if_null() {
		String html = null;
		String htmlSanitized = "";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(htmlSanitized, sanitized);
	}
	
	@Test
	public void should_return_empty_sanitized_text_if_empty() {
		String html = "";
		String sanitized = htmlSanitizer.sanitize(html).getText();
		assertEquals(html, sanitized);
	}
	
	private void envReturns(String key, String answer) {
		when(env.has(key)).thenReturn(true);
		when(env.get(key)).thenReturn(answer);
		
	}

}
