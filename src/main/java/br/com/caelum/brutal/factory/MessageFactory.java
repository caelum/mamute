package br.com.caelum.brutal.factory;

import br.com.caelum.vraptor4.core.Localization;
import br.com.caelum.vraptor4.validator.I18nMessage;

@Component
public class MessageFactory {
	private final Localization localization;

	public MessageFactory(Localization localization) {
		this.localization = localization;
	}
	
	public I18nMessage build(String category, String key, Object... parameters) {
		I18nMessage message = new I18nMessage(category, key, parameters);
		message.setBundle(localization.getBundle());
		return message;
	}
	
}
