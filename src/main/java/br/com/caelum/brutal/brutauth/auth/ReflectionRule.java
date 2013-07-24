package br.com.caelum.brutal.brutauth.auth;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

<<<<<<< HEAD
import br.com.caelum.brutal.brutauth.auth.rules.SimpleBrutauthRule;

public abstract class ReflectionRule {
	public abstract Method getMethod(SimpleBrutauthRule toInvoke, Object[] args);
	
	protected boolean invoke(SimpleBrutauthRule toInvoke, Object[] args) {
=======
import br.com.caelum.brutal.brutauth.auth.rules.BrutauthRule;

public abstract class ReflectionRule {
	public abstract Method getMethod(BrutauthRule toInvoke, Object[] args);
	
	protected boolean invoke(BrutauthRule toInvoke, Object[] args) {
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
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
