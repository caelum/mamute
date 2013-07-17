package br.com.caelum.brutal.brutauth.reflection;

import java.lang.reflect.Method;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultMethodInvoker {

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

		private Object[] getArgumentsThatMatchToTypes(Class[] types, Object[] args) {
			return args; //TODO Match args to types
		}

		private Method getMethod(String string, CustomBrutauthRule toInvoke) throws NoSuchMethodException {
			Method[] methods = toInvoke.getClass().getMethods();
			for (Method method : methods) {
				if(method.getName().equals("isAllowed")) return method; 
			}
			throw new NoSuchMethodException("Não existe o metodo default 'isAllowed' na classe: "+ toInvoke.getClass());
		}

		private Object[] fakeVarArgs() {
			Object[] objects = new Object[1];
			objects[0] = new Object[0];
			return objects;
		}

}
