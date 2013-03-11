package br.com.caelum.brutal.interceptors;

import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.infra.ThreadLocals;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts(before = ExecuteMethodInterceptor.class)
public class PimpMyControllerInterceptor implements Interceptor {

	private final Result result;
	private final MessageFactory factory;

	public PimpMyControllerInterceptor(Result result, MessageFactory factory) {
		this.result = result;
		this.factory = factory;
	}

	@Override
	public boolean accepts(ResourceMethod arg0) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
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
