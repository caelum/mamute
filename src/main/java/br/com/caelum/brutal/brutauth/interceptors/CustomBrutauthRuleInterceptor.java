package br.com.caelum.brutal.brutauth.interceptors;

<<<<<<< HEAD
import br.com.caelum.brutal.brutauth.auth.BrutauthReflectionComposedRule;
import br.com.caelum.brutal.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutal.brutauth.auth.handlers.HandlerSearcher;
import br.com.caelum.brutal.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutal.brutauth.reflection.DefaultMethodInvoker;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
=======
import static br.com.caelum.vraptor.view.Results.http;
import br.com.caelum.brutal.brutauth.auth.BrutauthReflectionComposedRule;
import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutal.brutauth.reflection.DefaultMethodInvoker;
import br.com.caelum.brutal.brutauth.rules.CustomBrutauthRules;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts(before=ExecuteMethodInterceptor.class, after=ParametersInstantiatorInterceptor.class)
public class CustomBrutauthRuleInterceptor implements Interceptor{

	private final Container container;
	private final MethodInfo methodInfo;
	private final DefaultMethodInvoker invoker;
<<<<<<< HEAD
	private final HandlerSearcher handlers;

	public CustomBrutauthRuleInterceptor(Container container, MethodInfo methodInfo,
			DefaultMethodInvoker invoker, HandlerSearcher handlers) {
		this.container = container;
		this.methodInfo = methodInfo;
		this.invoker = invoker;
		this.handlers = handlers;
=======
	private final Result result;

	public CustomBrutauthRuleInterceptor(Container container, MethodInfo methodInfo, DefaultMethodInvoker invoker, Result result) {
		this.container = container;
		this.methodInfo = methodInfo;
		this.invoker = invoker;
		this.result = result;
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
	}
	
	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		CustomBrutauthRules annotation = method.getMethod().getAnnotation(CustomBrutauthRules.class);
		Class<? extends CustomBrutauthRule>[] values = annotation.value();
		
		BrutauthReflectionComposedRule brutauthComposedRule = null;
		for (Class<? extends CustomBrutauthRule> value : values) {
			CustomBrutauthRule brutauthRule = container.instanceFor(value);
			brutauthComposedRule = compose(brutauthComposedRule, brutauthRule);
		}
		
		boolean allowed = brutauthComposedRule.isAllowed(methodInfo.getParameters());
<<<<<<< HEAD
		RuleHandler handler = handlers.getHandler(brutauthComposedRule);
		if(!handler.handle(allowed)){
=======
		if(!allowed){
			result.use(http()).sendError(403);
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
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
		return method.containsAnnotation(CustomBrutauthRules.class);
	}
}