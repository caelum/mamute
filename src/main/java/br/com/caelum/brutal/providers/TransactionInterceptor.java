package br.com.caelum.brutal.providers;

import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.brutal.infra.AfterSuccessfulTransaction;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.plugin.hibernate4.HibernateTransactionInterceptor;

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
			AfterSuccessfulTransaction afterTransaction, MutableResponse response) {
		super(session, validator, response);
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
