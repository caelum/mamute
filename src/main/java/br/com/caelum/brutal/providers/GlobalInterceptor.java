package br.com.caelum.brutal.providers;

import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

import br.com.caelum.brutal.model.User;
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
	private final User currentUser;

	public GlobalInterceptor(Environment env, Result result, User currentUser) {
		this.env = env;
		this.result = result;
		this.currentUser = currentUser;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		result.include("env", env);
		result.include("currentUser", currentUser);
		result.include("prettyTimeFormatter", new PrettyTime(new Locale("pt")));
		stack.next(method, resourceInstance);
	}

	public boolean accepts(ResourceMethod method) {
		return true;
	}

}
