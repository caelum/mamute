package br.com.caelum.brutal.brutauth.reflection.methodsearchers;

import java.lang.reflect.Method;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class BrutauthDefaultMethodSearcher {

	public Method getMethod(CustomBrutauthRule toInvoke) throws NoSuchMethodException {
		Method[] methods = toInvoke.getClass().getMethods();
		for (Method method : methods) {
			if(method.getName().equals("isAllowed") || method.isAnnotationPresent(BrutauthValidation.class)) return method; 
		}
		throw new NoSuchMethodException("Não existe o metodo default 'isAllowed' na classe: "+ toInvoke.getClass());
	}

}
