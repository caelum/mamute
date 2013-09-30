package br.com.caelum.brutal.controllers;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;

import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.I18nMessage;

public class BrutalValidator {

	private javax.validation.Validator javaxValidator;
	private Validator validator;

	@Deprecated
	public BrutalValidator() {
	}
	
	@Inject
	public BrutalValidator(javax.validation.Validator javaxValidator,
			Validator validator) {
		this.javaxValidator = javaxValidator;
		this.validator = validator;
	}

	public void validate(Object information) {
		Set<ConstraintViolation<Object>> erros = javaxValidator.validate(information);
		for (ConstraintViolation constraintViolation : erros) {
			validator.add(new I18nMessage("", constraintViolation.getMessage()));
		}
		
	}
	
}
