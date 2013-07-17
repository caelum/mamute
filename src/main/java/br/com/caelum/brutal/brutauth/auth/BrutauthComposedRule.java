package br.com.caelum.brutal.brutauth.auth;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.caelum.brutal.auth.rules.ComposedRule;
import br.com.caelum.brutal.auth.rules.PermissionRule;
import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;

public class BrutauthComposedRule {
		
		private CustomBrutauthRule current;
	
		public BrutauthComposedRule(CustomBrutauthRule first) {
			this.current = first;
		}
		
		public BrutauthComposedRule and(CustomBrutauthRule second) {
			current = new AndRule(current, second);
			return this;
		}
		
		public boolean isAllowed(Object...args) {
			return invoke(current, args);
		}

		public static <T> ComposedRule<T> composedRule(PermissionRule<T> first) {
			return new ComposedRule<>(first);
		}
		
		private static class AndRule implements CustomBrutauthRule {
			private final CustomBrutauthRule second;
			private final CustomBrutauthRule first;
			
			public AndRule(CustomBrutauthRule first, CustomBrutauthRule second) {
				this.first = first;
				this.second = second;
			}
			
			public boolean isAllowed(Object...objects) {
				return invoke(first, objects) && invoke(second, objects);
			}
		}

		private static boolean invoke(CustomBrutauthRule toInvoke, Object[] args) {
			Method defaultMethod = getDefaultMethod(toInvoke, args);
			try {
				if(defaultMethod.getParameterTypes()[0].isAssignableFrom(Object[].class)) args = new Object[]{args};
				return (boolean) defaultMethod.invoke(toInvoke, args);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new RuntimeException("vish, deu erro ai", e);
			}
		}

		private static Method getDefaultMethod(CustomBrutauthRule toInvoke, Object...args) {
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

		private static Object[] fakeVarArgs() {
			Object[] objects = new Object[1];
			objects[0] = new Object[0];
			return objects;
		}

		private static Class<?>[] getClasses(Object[] args) {
			Class<?>[] classes = new Class[args.length];
			for (int i = 0; i < classes.length; i++) {
				classes[i] = args[i].getClass();
			}
			return classes;
		}
		
}
