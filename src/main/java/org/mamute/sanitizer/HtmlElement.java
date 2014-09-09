package org.mamute.sanitizer;

import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ELEMENTS_KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Vetoed;

import br.com.caelum.vraptor.environment.Environment;

@Vetoed
public class HtmlElement {

	static final String COMMA = "(\\s)?+,(\\s)?+";
	private String element;
	private Map<String, String> attributesAndWhitelist;

	private HtmlElement(String element, Map<String, String> attributesAndWhitelist) {
		this.element = element;
		this.attributesAndWhitelist = attributesAndWhitelist;
	}

	public static List<HtmlElement> using(Environment environment) {
		String[] allowed = environment.get(ALLOWED_ELEMENTS_KEY).split(COMMA);
		List<HtmlElement> elements = new ArrayList<>();
		for (String element: allowed) {
			elements.add(new HtmlElement(element, HtmlAttribute.using(element, environment)));
		}
		return elements;
	}
	
	public String getElement() {
		return element;
	}

	public Map<String, String> getAttributesAndWhitelist() {
		return attributesAndWhitelist;
	}

}
