package br.com.caelum.brutal.interceptors;

import java.util.Arrays;
import java.util.List;

import org.hibernate.ObjectNotFoundException;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
public class ObjectNotFoundInterceptor implements Interceptor{
	
	private Result result;

	public ObjectNotFoundInterceptor(Result result) {
		this.result = result;
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		List<Class<?>> methodList = Arrays.asList(method.getMethod().getExceptionTypes());
		return methodList.contains(ObjectNotFoundException.class);
		
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object obj) throws InterceptionException {
		result.on(ObjectNotFoundException.class).notFound();
		stack.next(method, obj);
	}

}
