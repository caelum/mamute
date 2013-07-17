package br.com.caelum.brutal.brutauth.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;

public class BrutauthMethod {

	private final CustomBrutauthRule toInvoke;
	private final Method defaultMethod;
	private final Object[] arguments;

	public BrutauthMethod(Object[] arguments, Method defaultMethod, CustomBrutauthRule toInvoke) {
		this.arguments = arguments;
		this.defaultMethod = defaultMethod;
		this.toInvoke = toInvoke;
	}

	public boolean invoke() {
		try {
			return (boolean) defaultMethod.invoke(toInvoke, arguments);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException("Não consegui chamar o metodo "+ toInvoke.getClass().getSimpleName()+ "#" + defaultMethod.getName() + " com parametros dos tipos: "+ getTypes(arguments));
		}
	}

	private String getTypes(Object[] arguments) {
		StringBuilder types = new StringBuilder();
		for (Object argument : arguments) {
			types.append(" | "+ argument.getClass().getSimpleName());
		}
		return types.toString();
	}

}
