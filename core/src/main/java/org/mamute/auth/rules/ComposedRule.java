package org.mamute.auth.rules;

import org.mamute.model.User;

public class ComposedRule<T> implements PermissionRule<T> {
	
	private PermissionRule<T> current;
	
	public ComposedRule(PermissionRule<T> first) {
		this.current = first;
	}
	
	public ComposedRule<T> or(PermissionRule<T> second) {
		current = new OrRule<>(current, second);
		return this;
	}
	
	public ComposedRule<T> and(PermissionRule<T> second) {
		current = new AndRule<>(current, second);
		return this;
	}
	
	@Override
	public boolean isAllowed(User u, T item) {
		return current.isAllowed(u, item);
	}
	
	public static <T> ComposedRule<T> composedRule(PermissionRule<T> first) {
		return new ComposedRule<>(first);
	}
	
	
	private static class OrRule<T> implements PermissionRule<T> {
		private final PermissionRule<T> second;
		private final PermissionRule<T> first;

		public OrRule(PermissionRule<T> first, PermissionRule<T> second) {
			this.first = first;
			this.second = second;
		}

		@Override
		public boolean isAllowed(User u, T item) {
			return first.isAllowed(u, item) || second.isAllowed(u, item);
		}
		
	}
	
	private static class AndRule<T> implements PermissionRule<T> {
		private final PermissionRule<T> second;
		private final PermissionRule<T> first;
		
		public AndRule(PermissionRule<T> first, PermissionRule<T> second) {
			this.first = first;
			this.second = second;
		}
		
		@Override
		public boolean isAllowed(User u, T item) {
			return first.isAllowed(u, item) && second.isAllowed(u, item);
		}
	}
	
}
