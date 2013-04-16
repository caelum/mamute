package br.com.caelum.brutal.interceptors;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.http.MutableResponse.RedirectListener;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
public class RedirectCommitInterceptor implements Interceptor {
	private Session session;
	private MutableResponse response;
	private final Validator validator;

	public RedirectCommitInterceptor(Session session, MutableResponse response, Validator validator) {
		this.session = session;
		this.response = response;
		this.validator = validator;
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object instance) {
		response.addRedirectListener(new RedirectListener() {
			@Override
			public void beforeRedirect() {
				Transaction transaction = session.getTransaction();
				if (!validator.hasErrors() && transaction.isActive()) {
					session.flush();
				}
			}
		});
		stack.next(method, instance);
	}

}