package br.com.caelum.brutal.auth.rules;

import br.com.caelum.brutal.model.User;

public interface PermissionRule<T> {
    public boolean isAllowed(User u, T item);
}
