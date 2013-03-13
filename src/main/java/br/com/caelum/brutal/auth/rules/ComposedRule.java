package br.com.caelum.brutal.auth.rules;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.brutal.model.User;

public class ComposedRule<T> implements PermissionRule<T> {
	
	private PermissionRule<T> first;
	private List<PermissionRule<T>> ors = new ArrayList<>();
	private List<PermissionRule<T>> ands = new ArrayList<>();
	
	public ComposedRule<T> thiz(PermissionRule<T> first) {
		if (this.first != null)
			throw new IllegalStateException("already setted");
		this.first = first;
		return this;
	}
	
	public ComposedRule<T> or(PermissionRule<T> second) {
		ors.add(second);
		return this;
	}
	
	public ComposedRule<T> and(PermissionRule<T> second) {
		ands.add(second);
		return this;
	}
	
	@Override
	public boolean isAllowed(User u, T item) {
		boolean isAllowed = first.isAllowed(u, item);
		for (PermissionRule<T> or : ors) {
			isAllowed = isAllowed || or.isAllowed(u, item);
		}
		for (PermissionRule<T> and : ands) {
			isAllowed = isAllowed && and.isAllowed(u, item);
		}
		return isAllowed;
	}
	
}
