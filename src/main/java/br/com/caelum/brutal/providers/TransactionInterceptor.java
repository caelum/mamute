package br.com.caelum.brutal.providers;

import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.brutal.infra.AfterSuccessfulTransaction;
import br.com.caelum.vraptor.plugin.hibernate4.HibernateTransactionInterceptor;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.Validator;
import br.com.caelum.vraptor4.interceptor.SimpleInterceptorStack;

@Intercepts
@Specializes
public class TransactionInterceptor extends HibernateTransactionInterceptor {

	private Session session;
	private Validator validator;
	private AfterSuccessfulTransaction afterTransaction;
	
	@Deprecated //CDI eyes only
	public TransactionInterceptor() {
	}
	
	@Inject
	public TransactionInterceptor(Session session, Validator validator,
			AfterSuccessfulTransaction afterTransaction) {
		super(session, validator);
		this.session = session;
		this.validator = validator;
		this.afterTransaction = afterTransaction;
	}

	@Override
	public void intercept(SimpleInterceptorStack stack) {
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			stack.next();
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

}
