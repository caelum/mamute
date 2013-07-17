package br.com.caelum.brutal.brutauth.interceptors;

import static br.com.caelum.vraptor.view.Results.http;
import br.com.caelum.brutal.brutauth.auth.BrutauthReflectionComposedRule;
import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutal.brutauth.rules.AuthRules;
import br.com.caelum.brutal.brutauth.util.DefaultMethodInvoker;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.view.Results;

@Intercepts(before=ExecuteMethodInterceptor.class, after=ParametersInstantiatorInterceptor.class)
public class BrutalAuthInterceptor implements Interceptor{

	private final Container container;
	private final MethodInfo methodInfo;
	private final DefaultMethodInvoker invoker;
	private final Result result;

	public BrutalAuthInterceptor(Container container, MethodInfo methodInfo, DefaultMethodInvoker invoker, Result result) {
		this.container = container;
		this.methodInfo = methodInfo;
		this.invoker = invoker;
		this.result = result;
	}
	
	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		AuthRules annotation = method.getMethod().getAnnotation(AuthRules.class);
		Class<? extends CustomBrutauthRule>[] values = annotation.value();
		
		BrutauthReflectionComposedRule brutauthComposedRule = null;
		for (Class<? extends CustomBrutauthRule> value : values) {
			CustomBrutauthRule brutauthRule = container.instanceFor(value);
			brutauthComposedRule = compose(brutauthComposedRule, brutauthRule);
		}
		
		boolean allowed = brutauthComposedRule.isAllowed(methodInfo.getParameters());
		if(!allowed){
			result.use(http()).sendError(403);
			return;
		}
		
		stack.next(method, resourceInstance);
	}

	private BrutauthReflectionComposedRule compose(BrutauthReflectionComposedRule brutauthComposedRule, CustomBrutauthRule brutauthRule) {
		if(brutauthComposedRule == null){
			brutauthComposedRule = new BrutauthReflectionComposedRule(brutauthRule, invoker);
		}else{
			brutauthComposedRule.and(brutauthRule);
		}
		return brutauthComposedRule;
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return method.containsAnnotation(AuthRules.class);
	}
}