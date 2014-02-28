package org.mamute.controllers;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;

import org.mamute.factory.MessageFactory;

import br.com.caelum.vraptor.validator.Validator;

public class BrutalValidator {

	private javax.validation.Validator javaxValidator;
	private Validator validator;
	private MessageFactory factory;

	@Deprecated
	public BrutalValidator() {
	}
	
	@Inject
	public BrutalValidator(javax.validation.Validator javaxValidator,
			Validator validator, MessageFactory factory) {
		this.javaxValidator = javaxValidator;
		this.validator = validator;
		this.factory = factory;
	}

	public void validate(Object information) {
		Set<ConstraintViolation<Object>> erros = javaxValidator.validate(information);
		for (ConstraintViolation constraintViolation : erros) {
			validator.add(factory.build("error", constraintViolation.getMessage()));
		}
		
	}
	
}
