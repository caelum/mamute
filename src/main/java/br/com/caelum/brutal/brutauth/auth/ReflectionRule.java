package br.com.caelum.brutal.brutauth.auth;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.caelum.brutal.brutauth.auth.rules.SimpleBrutauthRule;

public abstract class ReflectionRule {
	public abstract Method getMethod(SimpleBrutauthRule toInvoke, Object[] args);
	
	protected boolean invoke(SimpleBrutauthRule toInvoke, Object[] args) {
		Method defaultMethod = getMethod(toInvoke, args);
		try {
			if(defaultMethod.getParameterTypes()[0].isAssignableFrom(Object[].class)) args = new Object[]{args};
			return (boolean) defaultMethod.invoke(toInvoke, args);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException("vish, deu erro ai", e);
		}
	}
}
