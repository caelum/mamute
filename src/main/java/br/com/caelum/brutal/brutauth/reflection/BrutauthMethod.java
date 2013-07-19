package br.com.caelum.brutal.brutauth.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;

public class BrutauthMethod {

	private final CustomBrutauthRule toInvoke;
	private final Method defaultMethod;
	private final Object[] arguments;

	public BrutauthMethod(Object[] arguments, Method defaultMethod, CustomBrutauthRule toInvoke) throws NoSuchMethodException, SecurityException {
		toInvoke.getClass().getMethod(defaultMethod.getName(), getTypes(arguments));
		this.arguments = arguments;
		this.defaultMethod = defaultMethod;
		this.toInvoke = toInvoke;
	}

	public boolean invoke() {
		try {
			return (boolean) defaultMethod.invoke(toInvoke, arguments);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException("Não consegui chamar o metodo "+ toInvoke.getClass().getSimpleName()+ "#" + defaultMethod.getName() + " com parametros dos tipos: "+ getStringTypes(arguments));
		}
	}

	private String getStringTypes(Object[] arguments) {
		StringBuilder types = new StringBuilder();
		for (Object argument : arguments) {
			types.append(" | "+ argument.getClass().getSimpleName());
		}
		return types.toString();
	}

	private Class<?>[] getTypes(Object[] arguments) {
		Class<?>[] types = new Class[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			types[i] = arguments[i].getClass();
		}
		return types;
	}
	
}
