package br.com.caelum.brutal.sanitizer;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HtmlSanitizer {
	private static final PolicyFactory 
		POLICY = new HtmlPolicyBuilder()
		    .allowElements("a", "p", "pre", "code", "img", "kbd", "ol", "ul",
		    		"li", "strong", "h2", "blockquote", "hr")
		    .allowUrlProtocols("https", "http")
		    .allowAttributes("href").onElements("a")
		    .allowAttributes("class").onElements("pre")
		    .allowAttributes("src", "alt", "width", "height").onElements("img")
		    .requireRelNofollowOnLinks()
		    .toFactory();

	public static String sanitize(String html){
		return POLICY.sanitize(html);
	}
}
