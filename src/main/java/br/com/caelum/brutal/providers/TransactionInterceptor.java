package br.com.caelum.brutal.providers;

import org.hibernate.Session;

import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.util.hibernate.HibernateTransactionInterceptor;

@Intercepts
public class TransactionInterceptor extends HibernateTransactionInterceptor {

    public TransactionInterceptor(Session session, Validator validator) {
        super(session, validator);
    }
    
}
