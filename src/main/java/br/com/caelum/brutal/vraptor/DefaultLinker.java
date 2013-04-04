package br.com.caelum.brutal.vraptor;

import java.lang.reflect.Method;

import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.proxy.MethodInvocation;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.proxy.SuperMethod;

@ApplicationScoped
@Component
@SuppressWarnings({"rawtypes","unchecked"})
public class DefaultLinker implements Linker {

	public Method method;
	public Object[] args;
	private final Proxifier proxifier;
	private final Router router;
	private Class controllerType;
	private final Env env;

	DefaultLinker(Proxifier p, Router router, Env env) {
		this.proxifier = p;
		this.router = router;
		this.env = env;
	}

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
