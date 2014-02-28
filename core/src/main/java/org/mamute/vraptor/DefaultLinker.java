package org.mamute.vraptor;

import java.lang.reflect.Method;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.proxy.MethodInvocation;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.proxy.SuperMethod;

@ApplicationScoped
@SuppressWarnings({"rawtypes","unchecked"})
public class DefaultLinker implements Linker {

	@Inject private Proxifier proxifier;
	@Inject private Router router;
	@Inject private Env env;

	private Class controllerType;
	private Method method;
	private Object[] args;
	
	@Override
	public <T> T linkTo(T controller) {
		return (T) linkTo(controller.getClass());
	}
	@Override
	public <T> T linkTo(Class<T> controllerType) {
		this.controllerType = controllerType;
		MethodInvocation<T> invocation = new CacheInvocation();
		return proxifier.proxify(controllerType,invocation);
	}
	class CacheInvocation implements MethodInvocation{
		public Object intercept(Object proxy, Method method, Object[] args,
				SuperMethod superMethod) {
			DefaultLinker.this.method = method;
			DefaultLinker.this.args = args;
			return null;
		}
	}
	@Override
	public String get() {
		return env.getHostAndContext() + getRelativePath();
	}

	@Override
	public String getRelativePath() {
		return router.urlFor(controllerType, method, args);
	}

}
