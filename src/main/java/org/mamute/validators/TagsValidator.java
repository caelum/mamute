package org.mamute.validators;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.environment.Environment;
import org.mamute.factory.MessageFactory;
import org.mamute.model.Tag;

import br.com.caelum.vraptor.validator.Validator;

import static org.mamute.infra.NormalizerBrutal.toSlug;

public class TagsValidator {

	private final Environment env;
	private final Validator validator;
	private final MessageFactory messageFactory;

	@Deprecated
	public TagsValidator() {
		this(null, null, null);
	}

	@Inject
	public TagsValidator(Environment env, Validator validator, MessageFactory messageFactory) {
		this.env = env;
		this.validator = validator;
		this.messageFactory = messageFactory;
	}


	public boolean validate(List<Tag> found, List<String> wanted) {
		//prevent misleading errors if tags were improper earlier
		if (validator.hasErrors()) {
			return false;
		}

		for (String name : wanted) {
            if (!isPresent(name, found)) {
				validator.add(messageFactory.build("error", "tag.errors.doesnt_exist", name));
			}
		}
		return !validator.hasErrors();
	}

	private boolean isPresent(String name, List<Tag> found) {
		for (Tag tag : found) {
            String sluggedName = toSlug(name);
			if (tag.getUriName().equals(sluggedName))
				return true;
		}
		return false;
	}

	public <T> T onErrorRedirectTo(T controller) {
		return validator.onErrorRedirectTo(controller);
	}

}
