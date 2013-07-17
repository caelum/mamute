package br.com.caelum.brutal.brutauth.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;

public class BrutauthMethod {

	private final CustomBrutauthRule toInvoke;
	private final Method defaultMethod;
	private final Object[] arguments;

<<<<<<< HEAD
	public BrutauthMethod(Object[] arguments, Method defaultMethod, CustomBrutauthRule toInvoke) throws NoSuchMethodException, SecurityException {
		toInvoke.getClass().getMethod(defaultMethod.getName(), getTypes(arguments));
=======
	public BrutauthMethod(Object[] arguments, Method defaultMethod, CustomBrutauthRule toInvoke) {
>>>>>>> base para usar apenas objetos de classes que a rule receber no isAllowed
		this.arguments = arguments;
		this.defaultMethod = defaultMethod;
		this.toInvoke = toInvoke;
	}

	public boolean invoke() {
		try {
			return (boolean) defaultMethod.invoke(toInvoke, arguments);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
<<<<<<< HEAD
			throw new RuntimeException("Não consegui chamar o metodo "+ toInvoke.getClass().getSimpleName()+ "#" + defaultMethod.getName() + " com parametros dos tipos: "+ getStringTypes(arguments));
		}
	}

	private String getStringTypes(Object[] arguments) {
=======
			throw new RuntimeException("Não consegui chamar o metodo "+ toInvoke.getClass().getSimpleName()+ "#" + defaultMethod.getName() + " com parametros dos tipos: "+ getTypes(arguments));
		}
	}

	private String getTypes(Object[] arguments) {
>>>>>>> base para usar apenas objetos de classes que a rule receber no isAllowed
		StringBuilder types = new StringBuilder();
		for (Object argument : arguments) {
			types.append(" | "+ argument.getClass().getSimpleName());
		}
		return types.toString();
	}

<<<<<<< HEAD
	private Class<?>[] getTypes(Object[] arguments) {
		Class<?>[] types = new Class[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			types[i] = arguments[i].getClass();
		}
		return types;
	}
	
=======
>>>>>>> base para usar apenas objetos de classes que a rule receber no isAllowed
}
