package br.com.caelum.brutal.auth.rules;

import br.com.caelum.brutal.model.User;

public class ComposedRule<T> implements PermissionRule<T> {
	
	private PermissionRule<T> first;
	private PermissionRule<T> second;
	private boolean isOr;
	
	public ComposedRule<T> thiz(PermissionRule<T> first) {
		this.first = first;
		return this;
	}
	
	public ComposedRule<T> or(PermissionRule<T> second) {
		this.second = second;
		isOr = true;
		return this;
	}
	
	public ComposedRule<T> and(PermissionRule<T> second) {
		this.second = second;
		isOr = false;
		return this;
	}
	
	@Override
	public boolean isAllowed(User u, T item) {
		if (isOr) 
			return first.isAllowed(u, item) || second.isAllowed(u, item); 
		return first.isAllowed(u, item) && second.isAllowed(u, item);
	}
	
}
