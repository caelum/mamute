package br.com.caelum.brutal.brutauth.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.vraptor.http.ParametersProvider;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultMethodInvoker {
		private final ParametersProvider parametersProvider;

		public DefaultMethodInvoker(ParametersProvider parametersProvider) {
			this.parametersProvider = parametersProvider;
		}
	
		public boolean invoke(CustomBrutauthRule toInvoke, Object[] args) {
			Method defaultMethod = getDefaultMethod(toInvoke, args);
			try {
				if(defaultMethod.getParameterTypes()[0].isAssignableFrom(Object[].class)) args = new Object[]{args};
				return (boolean) defaultMethod.invoke(toInvoke, args);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new RuntimeException("vish, deu erro ai", e);
			}
		}
		
		private Method getDefaultMethod(CustomBrutauthRule toInvoke, Object...args) {
			Class<?>[] classes = getClasses(args);
			Method method;
			try {
				return toInvoke.getClass().getMethod("isAllowed", classes);
			} catch (NoSuchMethodException e) {
				if(classes[0].isAssignableFrom(fakeVarArgs().getClass()))
					throw new RuntimeException("Não existe o metodo default 'isAllowed' na classe: "+ toInvoke.getClass());
				return getDefaultMethod(toInvoke, fakeVarArgs());
			}
		}

		private Object[] fakeVarArgs() {
			Object[] objects = new Object[1];
			objects[0] = new Object[0];
			return objects;
		}

		private Class<?>[] getClasses(Object[] args) {
			Class<?>[] classes = new Class[args.length];
			for (int i = 0; i < classes.length; i++) {
				classes[i] = args[i].getClass();
			}
			return classes;
		}
}
