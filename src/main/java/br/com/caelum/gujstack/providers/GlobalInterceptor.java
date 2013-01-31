package br.com.caelum.gujstack.providers;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Component
@Intercepts(before=ParametersInstantiatorInterceptor.class)
public class GlobalInterceptor implements Interceptor{
	
	private final Environment env;
	private final Result result;

	public GlobalInterceptor(Environment env, Result result) {
		this.env = env;
		this.result = result;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		// TODO pode remover com servlet 3.0 pois pode chamar na view
		result.include("analyticsGnarus", env.get("analytics.gnarus"));
		result.include("analyticsCaelumsite", env.get("analytics.caelumsite"));
		result.include("env", env);
		stack.next(method, resourceInstance);
	}

	public boolean accepts(ResourceMethod method) {
		return true;
	}

}
