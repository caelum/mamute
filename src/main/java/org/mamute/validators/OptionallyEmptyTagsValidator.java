package org.mamute.validators;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.mamute.model.Tag;

import br.com.caelum.vraptor.environment.Environment;

@Dependent
public class OptionallyEmptyTagsValidator implements ConstraintValidator<OptionallyEmptyTags, List<Tag>> {
	
	@Inject private Environment env;

	@Override
	public boolean isValid(List<Tag> tags, ConstraintValidatorContext context) {
		if(env == null) return true;
		return !env.supports("feature.tags.mandatory") || !tags.isEmpty();
	}

	@Override
	public void initialize(OptionallyEmptyTags constraintAnnotation) {
	}

}
