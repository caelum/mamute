package br.com.caelum.brutal.interceptors;

import javax.inject.Inject;

import br.com.caelum.vraptor4.InterceptionException;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.Validator;
import br.com.caelum.vraptor4.core.InterceptorStack;
import br.com.caelum.vraptor4.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor4.interceptor.Interceptor;
import br.com.caelum.vraptor4.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor4.restfulie.controller.ControllerMethod;
import br.com.caelum.vraptor4.view.Results;

@Intercepts(after=ParametersInstantiatorInterceptor.class, before=ExecuteMethodInterceptor.class)
public class ConversionErrorsInterceptor implements Interceptor {
	
	@Inject private Validator validator;

	@Override
	public boolean accepts(ControllerMethod arg0) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method, Object obj) throws InterceptionException {
		validator.onErrorUse(Results.http()).sendError(400, "there was conversion errors, " +
				"check your request!");
		stack.next(method, obj);
	}

}
