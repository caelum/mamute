package br.com.caelum.brutal.providers;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.util.hibernate.HibernateTransactionInterceptor;

@Intercepts
public class TransactionInterceptor extends HibernateTransactionInterceptor {

    private final HttpServletRequest request;

	public TransactionInterceptor(Session session, Validator validator, HttpServletRequest request) {
        super(session, validator);
        this.request = request;
    }
    
    @Override
    public boolean accepts(ResourceMethod method) {
    	return request.getMethod().equals("POST") || method.containsAnnotation(RequiresTransaction.class);
    }
    
}
