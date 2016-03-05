package org.mamute.sanitizer;

import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ELEMENTS_KEY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.owasp.html.ElementPolicy;

import br.com.caelum.vraptor.environment.Environment;

public class HtmlElementsBuilder {
	static final String COMMA = "(\\s)?+,(\\s)?+";
	private Environment env;
	private HtmlAttributesBuilder builder;
	private Map<String, ElementPolicy> elementSpecificPolicies;

	/**
	 * @deprecated CDI eyes only
	 */
	public HtmlElementsBuilder() {
	}
	
	@Inject
	public HtmlElementsBuilder(Environment env, HtmlAttributesBuilder builder) {
		this.env = env;
		this.builder = builder;
	}

	@PostConstruct
	public void setUp() {
		elementSpecificPolicies = new HashMap<String, ElementPolicy>();
		elementSpecificPolicies.put("a", new HtmlLinkTargetElementPolicy());
	}

	public List<HtmlElement> build() {
		String[] allowed = env.get(ALLOWED_ELEMENTS_KEY).split(COMMA);
		List<HtmlElement> elements = new ArrayList<>();
		for (String element: allowed) {
			final ElementPolicy policy = elementSpecificPolicies.get(element);
			elements.add(new HtmlElement(element, policy, builder.build(element)));
		}

		return elements;
	}

}
