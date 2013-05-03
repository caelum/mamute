package br.com.caelum.brutal.sanitizer;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HtmlSanitizer {
	private static final PolicyFactory 
		POLICY = new HtmlPolicyBuilder()
		    .allowElements("a", "blockquote", "code", "em", "h1", "h2", "hr", "img", 
		    		"kbd", "li", "ol", "p", "pre", "strong", "ul")
		    .allowUrlProtocols("https", "http")
		    .allowAttributes("href").onElements("a")
		    .allowAttributes("class").onElements("pre")
		    .allowAttributes("src", "alt", "width", "height").onElements("img")
		    .requireRelNofollowOnLinks()
		    .toFactory();

	public static String sanitize(String html){
		return html == null ? null:POLICY.sanitize(html);
	}
}
