package org.mamute.auth.rules;

import org.mamute.model.User;

public interface PermissionRule<T> {
    public boolean isAllowed(User u, T item);
}
