package br.com.caelum.brutal.validators;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.validation.MessageInterpolator;
import javax.validation.Validator;

import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.validator.beanvalidation.DefaultBeanValidator;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class CustomJSR303Validator extends DefaultBeanValidator {

	private MessageFactory messageFactory;

	@Deprecated
	public CustomJSR303Validator() {
	}

	@Inject
	public CustomJSR303Validator(Localization localization,
			Validator validator, MessageInterpolator interpolator, 
			MessageFactory messageFactory) {
		super(localization, validator, interpolator);
		this.messageFactory = messageFactory;
	}

	@Override
	public List<Message> validate(Object bean, Class<?>... groups) {
		List<Message> validationMessages = super.validate(bean, groups);
		return toI18n(validationMessages);
	}

	
	@Override
	public List<Message> validateProperties(Object bean, String... properties) {
		List<Message> messages = super.validateProperties(bean, properties);
		return toI18n(messages);
	}

	private ArrayList<Message> toI18n(List<Message> validationMessages) {
		ArrayList<Message> messages = new ArrayList<Message>();
		for (Message message : validationMessages) {
			messages.add(messageFactory.build("error", message.getMessage()));
		}
		return messages;
	}
}
