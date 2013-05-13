package br.com.caelum.brutal.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.caelum.brutal.sanitizer.HtmlSanitizer;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.proxy.MethodInvocation;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.proxy.SuperMethod;

@Component
public class ContentProxifier {
	
	private final Proxifier proxifier;

	public ContentProxifier(Proxifier proxifier) {
		this.proxifier = proxifier;
	}
	
	public <T> T safeProxyFor(final T object, Class<T> clazz) {
		T proxied = proxifier.proxify(clazz, new MethodInvocation<T>() {
			@Override
			public Object intercept(T proxy, Method method, Object[] args, SuperMethod superMethod) {
				if (method.getReturnType().equals(String.class)) {
					try {
						String result = (String) method.invoke(object, args);
						return HtmlSanitizer.sanitize(result);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				}
				return superMethod.invoke(proxy, args);
			}
		});
		return proxied;
	}

}
