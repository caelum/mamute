package org.mamute.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;

import org.mamute.sanitizer.HtmlSanitizer;

import br.com.caelum.vraptor.proxy.MethodInvocation;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.proxy.SuperMethod;

public class ContentProxifier {
	
	private Proxifier proxifier;
	private HtmlSanitizer sanitizer;

	@Deprecated
	public ContentProxifier() {
	}
	
	@Inject
	public ContentProxifier(Proxifier proxifier, HtmlSanitizer sanitizer) {
		this.proxifier = proxifier;
		this.sanitizer = sanitizer;
	}

	public <T> T safeProxyFor(final T object, Class<T> clazz) {
		T proxied = proxifier.proxify(clazz, new MethodInvocation<T>() {
			@Override
			public Object intercept(T proxy, Method method, Object[] args, SuperMethod superMethod) {
				if (method.getReturnType().equals(String.class)) {
					try {
						String result = (String) method.invoke(object, args);
						return sanitizer.sanitize(result);
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
