package br.com.caelum.brutal.brutauth.reflection.methodsearchers;

import java.lang.reflect.Method;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutal.brutauth.reflection.BrutauthMethod;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class BrutauthAdaptedMethodSearcher implements BrutauthMethodSearcher {

	private final BrutauthDefaultMethodSearcher defaultMethodSearcher;

	public BrutauthAdaptedMethodSearcher(BrutauthDefaultMethodSearcher defaultMethodSearcher) {
		this.defaultMethodSearcher = defaultMethodSearcher;
	}
	
	@Override
	public BrutauthMethod search(CustomBrutauthRule ruleToSearch, Object... withArgs) {
		try {
			Method defaultMethod = defaultMethodSearcher.getMethod(ruleToSearch);
			Class<?>[] classes = defaultMethod.getParameterTypes();
			return new BrutauthMethod(getArgumentsThatMatchToTypes(classes, withArgs), defaultMethod, ruleToSearch);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	private Object[] getArgumentsThatMatchToTypes(Class<?>[] types, Object[] args) throws NoSuchMethodException {
		Object[] argsToUse = new Object[types.length];
		for (int i = 0; i < types.length; i++) {
			for (Object arg : args) {
				if(arg.getClass().isAssignableFrom(types[i])){
					argsToUse[i] = arg;
				}
			}
			if(argsToUse[i] == null) throw new NoSuchMethodException("O metodo do seu controller não recebe todos os argumentos que o isAllowed espera!");
		}
		return argsToUse;
	}
}
