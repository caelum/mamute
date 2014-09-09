package org.mamute.sanitizer;

import static java.util.regex.Pattern.compile;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlPolicyBuilder.AttributeBuilder;
import org.owasp.html.PolicyFactory;

import br.com.caelum.vraptor.environment.Environment;

@ApplicationScoped
public class HtmlSanitizer {

	static final String ALLOWED_ELEMENTS_KEY = "sanitizer.allowed_elements";
	static final String ALLOWED_ATTRIBUTES_KEY_PREFIX = "sanitizer.allowed_attributes.";
	static final String ALLOWED_ATTRIBUTES_WHITELIST_KEY_SUFIX = ".whitelist.";
	
	private Environment env;
	private PolicyFactory policy;

	/**
	 * @deprecated CDI eyes only
	 */
	protected HtmlSanitizer() {
	}

	@Inject
	public HtmlSanitizer(Environment env) {
		this.env = env;
	}
	
	@PostConstruct
	public void setUp(){
		List<HtmlElement> allowedElements = HtmlElement.using(env);
		HtmlPolicyBuilder builder = new HtmlPolicyBuilder();
		for (HtmlElement htmlElement : allowedElements) {
			String elementName = htmlElement.getElement();
			builder.allowElements(elementName);
			
			Map<String, String> attributesAndWhitelist = htmlElement.getAttributesAndWhitelist();
			Set<String> allowedAttributes = attributesAndWhitelist.keySet();
			
			AttributeBuilder attributesBuilder = builder.allowAttributes(allowedAttributes.toArray(new String[]{}));

			for (String attribute : allowedAttributes) {
				String regex = attributesAndWhitelist.get(attribute);
				if(regex != null){
					attributesBuilder.matching(compile(regex));
					continue;
				}
			}
			attributesBuilder.onElements(elementName);
			
		}
		policy = builder
			.allowUrlProtocols("https", "http")
		    .requireRelNofollowOnLinks()
		    .toFactory();
	}

	public String sanitize(String html){
		return html == null ? null:policy.sanitize(html);
	}
}
