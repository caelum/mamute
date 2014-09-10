package org.mamute.sanitizer;

import static org.mamute.sanitizer.HtmlElementsBuilder.COMMA;
import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ATTRIBUTES_KEY_PREFIX;
import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ATTRIBUTES_WHITELIST_KEY_SUFIX;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import br.com.caelum.vraptor.environment.Environment;

public class HtmlAttributesBuilder {
	private Environment env;

	/**
	 * @deprecated CDI eyes only
	 */
	public HtmlAttributesBuilder() {
	}
	
	@Inject
	public HtmlAttributesBuilder(Environment env) {
		this.env = env;
	}

	public Map<String, String> build(String element) {
		Map<String, String> attributesAndWhitelist = new HashMap<>();
		String allowedAttributesKey = ALLOWED_ATTRIBUTES_KEY_PREFIX+element;
		if(env.has(allowedAttributesKey)){
			String allowedString = env.get(allowedAttributesKey);
			String[] allowedAttributes = allowedString.split(COMMA);
			for (String attribute : allowedAttributes) {
				String whitelistKey = allowedAttributesKey+ALLOWED_ATTRIBUTES_WHITELIST_KEY_SUFIX+attribute;
				String whitelist = env.get(whitelistKey, null);
				attributesAndWhitelist.put(attribute, whitelist);
			}
		}
		return attributesAndWhitelist;
	}
	
	
}
