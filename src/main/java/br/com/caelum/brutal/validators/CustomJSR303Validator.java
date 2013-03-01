package br.com.caelum.brutal.validators;

import java.util.ArrayList;
import java.util.List;

import javax.validation.MessageInterpolator;
import javax.validation.Validator;

import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.JSR303Validator;
import br.com.caelum.vraptor.validator.Message;

@Component
public class CustomJSR303Validator extends JSR303Validator{

	public CustomJSR303Validator(Localization localization,
			Validator validator, MessageInterpolator interpolator) {
		super(localization, validator, interpolator);
	}

	@Override
	public List<Message> validate(Object bean) {
		List<Message> messages = super.validate(bean);
		ArrayList<Message> i18nMessages = new ArrayList<Message>();
		for (Message message : messages) {
			i18nMessages.add(new I18nMessage(message.getCategory(), message.getMessage()));
		}
		
		return i18nMessages;
	}

}
