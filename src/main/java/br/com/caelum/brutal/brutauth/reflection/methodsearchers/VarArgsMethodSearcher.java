package br.com.caelum.brutal.brutauth.reflection.methodsearchers;

import java.lang.reflect.Method;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutal.brutauth.reflection.BrutauthMethod;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class VarArgsMethodSearcher implements MethodSearcher {

	private final DefaultMethodSearcher defaultMethodSearcher;

	public VarArgsMethodSearcher(DefaultMethodSearcher defaultMethodSearcher) {
		this.defaultMethodSearcher = defaultMethodSearcher;
	}
	
	@Override
	public BrutauthMethod search(CustomBrutauthRule ruleToSearch,
			Object... withArgs) {
		return getBrutalMethodWithParametersToInvoke(ruleToSearch, withArgs);
	}
	
	private BrutauthMethod getBrutalMethodWithParametersToInvoke(CustomBrutauthRule toInvoke, Object...args) {
		try {
			Method defaultMethod = defaultMethodSearcher.getMethod(toInvoke);
			return new BrutauthMethod(fakeVarArgs(args), defaultMethod, toInvoke);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	private Object[] fakeVarArgs(Object[] args) {
		return new Object[]{args};
	}
}
