package org.mamute.sanitizer;

import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ATTRIBUTES_KEY_PREFIX;
import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ATTRIBUTES_WHITELIST_KEY_SUFIX;

import java.util.HashMap;
import java.util.Map;

import br.com.caelum.vraptor.environment.Environment;


public class HtmlAttribute {


	public static Map<String, String> using(String element, Environment environment){
		Map<String, String> attributesAndWhitelist = new HashMap<>();
		String allowedString = environment.get(ALLOWED_ATTRIBUTES_KEY_PREFIX+element);
		if(allowedString != null){
			String[] allowedAttributes = allowedString.split(HtmlElement.COMMA);
			for (String attribute : allowedAttributes) {
				String whitelist = environment.get(ALLOWED_ATTRIBUTES_KEY_PREFIX+element+ALLOWED_ATTRIBUTES_WHITELIST_KEY_SUFIX+attribute);
				attributesAndWhitelist.put(attribute, whitelist);
			}
		}
		return attributesAndWhitelist;
	}

}
