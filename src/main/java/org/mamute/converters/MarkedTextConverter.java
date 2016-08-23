package org.mamute.converters;

import static org.mamute.model.MarkDown.parse;
import static org.mamute.model.MarkedText.pureAndMarked;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.mamute.model.MarkedText;
import org.mamute.sanitizer.HtmlSanitizer;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.converter.Converter;

@Convert(MarkedText.class)
@ApplicationScoped
public class MarkedTextConverter implements Converter<MarkedText> {

	private final HtmlSanitizer sanitizer;

	/**
	 * @deprecated CDI eyes only
	 */
	protected MarkedTextConverter() {
		this(null);
	}
	
	@Inject
	public MarkedTextConverter(HtmlSanitizer sanitizer) {
		this.sanitizer = sanitizer;
	}
	
	@Override
	public MarkedText convert(String value, Class<? extends MarkedText> type) {
		return pureAndMarked(value, sanitizer.sanitize(parse(value)).getText());
	}

}
