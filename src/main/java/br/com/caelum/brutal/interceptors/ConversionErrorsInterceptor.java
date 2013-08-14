package br.com.caelum.brutal.interceptors;

import br.com.caelum.vraptor4.InterceptionException;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.core.InterceptorStack;
import br.com.caelum.vraptor4.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor4.interceptor.ParametersInstantiatorInterceptor;

@Intercepts(after=ParametersInstantiatorInterceptor.class, before=ExecuteMethodInterceptor.class)
public class ConversionErrorsInterceptor implements Interceptor {
	
	private final Validator validator;

	public ConversionErrorsInterceptor(Validator validator) {
		this.validator = validator;
	}

	@Override
	public boolean accepts(ResourceMethod arg0) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object obj) throws InterceptionException {
		validator.onErrorUse(Results.http()).sendError(400, "there was conversion errors, " +
				"check your request!");
		stack.next(method, obj);
	}

}
