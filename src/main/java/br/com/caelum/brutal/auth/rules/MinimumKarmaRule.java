package br.com.caelum.brutal.auth.rules;

import br.com.caelum.brutal.model.User;

public class MinimumKarmaRule<T> implements PermissionRule<T> {
    
    private final int minimum;

    public MinimumKarmaRule(int minimum) {
        this.minimum = minimum;
    }

    @Override
    public boolean isAllowed(User u, T item) {
        return minimum <= u.getKarma();
    }

}
