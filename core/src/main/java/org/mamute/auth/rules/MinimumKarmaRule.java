package org.mamute.auth.rules;

import org.mamute.model.User;

public class MinimumKarmaRule<T> implements PermissionRule<T> {
    
    private final long minimum;

    public MinimumKarmaRule(long minimum) {
    	this.minimum = minimum;
    }

    @Override
    public boolean isAllowed(User u, T item) {
    	return u != null && minimum <= u.getKarma();
    }

}
