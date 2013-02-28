package br.com.caelum.brutal.sanitizer;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HtmlSanitizer {
	private static PolicyFactory 
		policy = new HtmlPolicyBuilder()
		    .allowElements("a", "p", "pre", "code")
		    .allowUrlProtocols("https", "http")
		    .allowAttributes("href").onElements("a")
		    .requireRelNofollowOnLinks()
		    .toFactory();

	public static String sanitize(String html){
		return policy.sanitize(html);
	}
}
