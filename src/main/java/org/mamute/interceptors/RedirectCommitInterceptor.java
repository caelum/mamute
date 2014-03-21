package org.mamute.interceptors;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.http.MutableResponse.RedirectListener;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.validator.Validator;

@Intercepts
public class RedirectCommitInterceptor implements Interceptor {
	@Inject private Session session;
	@Inject private MutableResponse response;
	@Inject private Validator validator;

	@Override
	public boolean accepts(ControllerMethod method) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method,
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