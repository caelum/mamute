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
		//TODO: validation is called before session.save, and cdi does not inject anything, it might be a vraptor/cdi issue 
		//(see https://groups.google.com/forum/#!topic/caelum-vraptor/qc7rvFpZLUw)
		if (env == null) {
			return true;
		}
		String mandatoryTags = env.get("tags.mandatory", "false");
		if (mandatoryTags.equals("false")) {
			return true;
		}
		return !tags.isEmpty();
	}

	@Override
	public void initialize(OptionallyEmptyTags constraintAnnotation) {
	}

}
