package br.com.caelum.brutal.sanitizer;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HtmlSanitizer {
	private static PolicyFactory 
		policy = new HtmlPolicyBuilder()
		    .allowElements("a", "p", "pre", "code", "img", "kbd", "ol", "ul",
		    		"li", "strong", "h2", "blockquote", "hr")
		    .allowUrlProtocols("https", "http")
		    .allowAttributes("href").onElements("a")
		    .allowAttributes("src", "alt").onElements("img")
		    .requireRelNofollowOnLinks()
		    .toFactory();

	public static String sanitize(String html){
		return policy.sanitize(html);
	}
}
