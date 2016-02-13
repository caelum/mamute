package org.mamute.sanitizer;

import static org.mamute.sanitizer.HtmlSanitizer.ALLOWED_ELEMENTS_KEY;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.environment.Environment;

public class HtmlElementsBuilder {
	static final String COMMA = "(\\s)?+,(\\s)?+";
	private Environment env;
	private HtmlAttributesBuilder builder;

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
	
	public List<HtmlElement> build() {
		String[] allowed = env.get(ALLOWED_ELEMENTS_KEY).split(COMMA);
		List<HtmlElement> elements = new ArrayList<>();
		for (String element: allowed) {
			elements.add(new HtmlElement(element, builder.build(element)));
		}
		return elements;
	}

	
}
