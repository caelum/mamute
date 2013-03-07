package br.com.caelum.brutal.validators;

import java.util.ArrayList;
import java.util.List;

import javax.validation.MessageInterpolator;
import javax.validation.Validator;

import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.JSR303Validator;
import br.com.caelum.vraptor.validator.Message;

@Component
public class CustomJSR303Validator extends JSR303Validator{

	private MessageFactory messageFactory;

	public CustomJSR303Validator(Localization localization,
			Validator validator, MessageInterpolator interpolator, MessageFactory messageFactory) {
		super(localization, validator, interpolator);
		this.messageFactory = messageFactory;
	}

	@Override
	public List<Message> validate(Object bean) {
		List<Message> validationMessages = super.validate(bean);
		ArrayList<Message> messages = new ArrayList<Message>();
		for (Message message : validationMessages) {
			messages.add(messageFactory.build("error", message.getMessage()));
		}
		return messages;
	}

}
