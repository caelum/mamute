package br.com.caelum.brutal.factory;

import javax.inject.Inject;

import br.com.caelum.vraptor4.core.Localization;
import br.com.caelum.vraptor4.validator.I18nMessage;

public class MessageFactory {
	private Localization localization;

	@Deprecated
	public MessageFactory() {
	}
	
	@Inject
	public MessageFactory(Localization localization) {
		this.localization = localization;
	}

	public I18nMessage build(String category, String key, Object... parameters) {
		I18nMessage message = new I18nMessage(category, key, parameters);
		message.setBundle(localization.getBundle());
		return message;
	}
	
}
