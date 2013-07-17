package br.com.caelum.brutal.brutauth.reflection;

import java.lang.reflect.Method;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultMethodInvoker {

		private final BrutauthMethodSearchers searcher;

		public DefaultMethodInvoker(BrutauthMethodSearchers searcher) {
			this.searcher = searcher;
		}
	
		public boolean invoke(CustomBrutauthRule toInvoke, Object[] args) {
			BrutauthMethod brutauthMethod = getBrutalMethodWithParametersToInvoke(toInvoke, args);
			return brutauthMethod.invoke();
		}
		
		private BrutauthMethod getBrutalMethodWithParametersToInvoke(CustomBrutauthRule toInvoke, Object...args) {
			try {
				Method defaultMethod = getMethod("isAllowed", toInvoke);
				Class<?>[] classes = defaultMethod.getParameterTypes();
				return new BrutauthMethod(getArgumentsThatMatchToTypes(classes, args), defaultMethod, toInvoke);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}

		private Object[] getArgumentsThatMatchToTypes(Class<?>[] types, Object[] args) {
			Object[] argsToUse = new Object[types.length];
			for (int i = 0; i < types.length; i++) {
				for (Object arg : args) {
					if(arg.getClass().isAssignableFrom(types[i])){
						argsToUse[i] = arg;
					}
				}
				if(argsToUse[i] == null) throw new IllegalArgumentException("O metodo do seu controller não recebe todos os argumentos que o isAllowed espera!");
			}
			return argsToUse;
		}

		private Method getMethod(String string, CustomBrutauthRule toInvoke) throws NoSuchMethodException {
			Method[] methods = toInvoke.getClass().getMethods();
			for (Method method : methods) {
				if(method.getName().equals("isAllowed")) return method; 
			}
			throw new NoSuchMethodException("Não existe o metodo default 'isAllowed' na classe: "+ toInvoke.getClass());
		}


}
