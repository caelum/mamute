package br.com.caelum.brutal.controllers;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

@Resource
public class VerifyUsersController {
	
	private final Session session;
	private final Logger LOG = Logger.getLogger(VerifyUsersController.class);

	public VerifyUsersController(Session session) {
		this.session = session;
	}
	
	@Post("/asjkdjnjsaknfknsdklglas")
	public void verify(){
		List<User> users = session.createCriteria(User.class).list();
		for (User user : users) {
			validateUser(user);
		}
	}

	private void validateUser(User user) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<User>> errors = validator.validate(user);
		if(!errors.isEmpty()){
			LOG.warn("The user "+user.getName()+" with id "+user.getId()+" has the errors bellow:");
			for (ConstraintViolation<User> constraintViolation : errors) {
				Object constraint = constraintViolation.getConstraintDescriptor().getAnnotation();
				LOG.warn(constraint);
			}
			LOG.warn("------------------------------------------------");
		}
	}

}
