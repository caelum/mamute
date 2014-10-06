package org.mamute.sanitizer;

import static org.mamute.model.SanitizedText.fromTrustedText;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.mamute.model.SanitizedText;
import org.owasp.html.PolicyFactory;

@ApplicationScoped
public class HtmlSanitizer {

	static final String ALLOWED_ELEMENTS_KEY = "sanitizer.allowed_elements";
	static final String ALLOWED_ATTRIBUTES_KEY_PREFIX = "sanitizer.allowed_attributes.";
	static final String ALLOWED_ATTRIBUTES_WHITELIST_KEY_SUFIX = ".whitelist.";
	
	private PolicyFactory policy;

	/**
	 * @deprecated CDI eyes only
	 */
	protected HtmlSanitizer() {
	}

	@Inject
	public HtmlSanitizer(PolicyFactory policy) {
		this.policy = policy;
	}
	
	public SanitizedText sanitize(String html){
		return fromTrustedText(policy.sanitize(html));
	}
}
