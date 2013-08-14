package br.com.caelum.brutal.providers;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.brutal.infra.AfterSuccessfulTransaction;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.core.InterceptorStack;

@Intercepts
public class TransactionInterceptor implements Interceptor {

	private final Session session;
	private final Validator validator;
	private final AfterSuccessfulTransaction afterTransaction;

	public TransactionInterceptor(Session session, Validator validator, AfterSuccessfulTransaction afterTransaction) {
		this.session = session;
		this.validator = validator;
		this.afterTransaction = afterTransaction;
    }
	
	public void intercept(InterceptorStack stack, ResourceMethod method, Object instance) {
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
    public boolean accepts(ResourceMethod method) {
    	return true;
    }
    
}
