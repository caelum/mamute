package br.com.caelum.brutal.interceptors;

import javax.inject.Inject;

import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.infra.ThreadLocals;
import br.com.caelum.vraptor4.InterceptionException;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.Result;
import br.com.caelum.vraptor4.controller.ControllerMethod;
import br.com.caelum.vraptor4.core.InterceptorStack;
import br.com.caelum.vraptor4.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor4.interceptor.Interceptor;

@Intercepts(before = ExecuteMethodInterceptor.class)
public class PimpMyControllerInterceptor implements Interceptor {

	@Inject private Result result;
	@Inject private MessageFactory factory;

	@Override
	public boolean accepts(ControllerMethod arg0) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method,
			Object obj) throws InterceptionException {
		try {
			locals.reset().put(Result.class, result).put(MessageFactory.class, factory);
			stack.next(method, obj);
		} finally {
			locals.clear();
		}
	}

	private static final ThreadLocals locals = new ThreadLocals();

	public static Result getResult() {
		return locals.get(Result.class);
	}

	public static MessageFactory getFactory() {
		return locals.get(MessageFactory.class);
	}

}
