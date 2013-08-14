package br.com.caelum.brutal.providers;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.brutal.infra.AfterSuccessfulTransaction;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.Validator;
import br.com.caelum.vraptor4.core.InterceptorStack;
import br.com.caelum.vraptor4.interceptor.Interceptor;
import br.com.caelum.vraptor4.restfulie.controller.ControllerMethod;

@Intercepts
public class TransactionInterceptor implements Interceptor {

	@Inject private Session session;
	@Inject private Validator validator;
	@Inject private AfterSuccessfulTransaction afterTransaction;

	
	public void intercept(InterceptorStack stack, ControllerMethod method, Object instance) {
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			stack.next(method, instance);
			if (!validator.hasErrors() && transaction.isActive()) {
				transaction.commit();
				afterTransaction.run();
			}
		} finally {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
		}
	}

	/**
	 * to avoid misterious bugs, every transaction should be inside a transaction, either get or post
	 */
    @Override
    public boolean accepts(ControllerMethod method) {
    	return true;
    }
    
}
