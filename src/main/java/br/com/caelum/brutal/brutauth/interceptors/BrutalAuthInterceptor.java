package br.com.caelum.brutal.brutauth.interceptors;

import br.com.caelum.brutal.brutauth.auth.BrutauthComposedRule;
import br.com.caelum.brutal.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutal.brutauth.rules.AuthRules;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts(before=ExecuteMethodInterceptor.class, after=ParametersInstantiatorInterceptor.class)
public class BrutalAuthInterceptor implements Interceptor{

	private final Container container;
	private final MethodInfo methodInfo;

	public BrutalAuthInterceptor(Container container, MethodInfo methodInfo) {
		this.container = container;
		this.methodInfo = methodInfo;
	}
	
	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		AuthRules annotation = method.getMethod().getAnnotation(AuthRules.class);
		Class<? extends BrutauthRule>[] values = annotation.value();
		
		BrutauthComposedRule brutauthComposedRule = null;
		for (Class<? extends BrutauthRule> value : values) {
			BrutauthRule brutauthRule = container.instanceFor(value);
			brutauthComposedRule = compose(brutauthComposedRule, brutauthRule);
		}
		
		boolean allowed = brutauthComposedRule.isAllowed(methodInfo.getParameters());
		System.out.println(allowed);
		
		stack.next(method, resourceInstance);
	}

	private BrutauthComposedRule compose(BrutauthComposedRule brutauthComposedRule, BrutauthRule brutauthRule) {
		if(brutauthComposedRule == null){
			brutauthComposedRule = new BrutauthComposedRule(brutauthRule);
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