package org.mamute.sanitizer;

import static java.util.regex.Pattern.compile;

import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.Vetoed;

import org.owasp.html.ElementPolicy;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlPolicyBuilder.AttributeBuilder;

import com.google.common.base.Optional;

@Vetoed
public class HtmlElement {

	private String element;
	private Optional<ElementPolicy> elementPolicy;
	private Map<String, String> attributesAndWhitelist;

	HtmlElement(String element, ElementPolicy elementPolicy, Map<String, String> attributesAndWhitelist) {
		this.element = element;
		this.elementPolicy = Optional.fromNullable(elementPolicy);
		this.attributesAndWhitelist = attributesAndWhitelist;
	}

	public String getElement() {
		return element;
	}

	public void configure(HtmlPolicyBuilder policyBuilder) {
		String elementName = getElement();

		if (elementPolicy.isPresent()) {
			policyBuilder.allowElements(elementPolicy.get(), elementName);
		} else {
			policyBuilder.allowElements(elementName);
		}

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
