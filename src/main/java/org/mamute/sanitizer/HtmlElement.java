package org.mamute.sanitizer;

import static java.util.regex.Pattern.compile;

import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.Vetoed;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlPolicyBuilder.AttributeBuilder;

@Vetoed
public class HtmlElement {

	private String element;
	private Map<String, String> attributesAndWhitelist;

	HtmlElement(String element, Map<String, String> attributesAndWhitelist) {
		this.element = element;
		this.attributesAndWhitelist = attributesAndWhitelist;
	}

	public String getElement() {
		return element;
	}

	public void configure(HtmlPolicyBuilder policyBuilder) {
		String elementName = getElement();
		policyBuilder.allowElements(elementName);
		
		Set<String> allowedAttributes = attributesAndWhitelist.keySet();
		AttributeBuilder attributesBuilder = policyBuilder.allowAttributes(allowedAttributes.toArray(new String[]{}));

		for (String attribute : allowedAttributes) {
			String regex = attributesAndWhitelist.get(attribute);
			if(regex != null){
				attributesBuilder.matching(compile(regex));
				continue;
			}
		}
		attributesBuilder.onElements(elementName);

		
	}

}
