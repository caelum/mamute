package br.com.caelum.brutal.controllers;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import net.vidageek.mirror.dsl.Mirror;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Validator;

@Resource
public class VerifyUsersController {
	
	private final Validator validator;
	private final Session session;
	private final Logger LOG = Logger.getLogger(VerifyUsersController.class);

	public VerifyUsersController(Session session, Validator validator) {
		this.session = session;
		this.validator = validator;
	}
	
	@Post("/asjkdjnjsaknfknsdklglas")
	public void verify(){
		List<User> users = session.createCriteria(User.class).list();
		for (User user : users) {
			validateUser(user);
		}
		validator.onErrorSendBadRequest();
	}

	private void validateUser(User user) {
		try{
			Transaction transaction = session.beginTransaction();
			new Mirror().on(user).set().field("about").withValue(user.getAbout()+" ");
			transaction.commit();
		}catch(ConstraintViolationException e){
			LOG.warn("The user "+user.getName()+" with id "+user.getId()+" has the constraintViolations bellow:");
			printViolations(e);
			LOG.warn("------------------------------------------------");
		}
	}

	private void printViolations(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		for (ConstraintViolation<?> constraintViolation : constraintViolations) {
			Object constraint = constraintViolation.getConstraintDescriptor().getAnnotation();
			LOG.warn(constraint);
		}
	}
}
