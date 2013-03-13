package br.com.caelum.brutal.auth.rules;

import br.com.caelum.brutal.model.User;

public class MinimumKarmaRule<T> implements PermissionRule<T> {
    
    private final long minimum;

    public MinimumKarmaRule(long minimum) {
        this.minimum = minimum;
    }

    @Override
    public boolean isAllowed(User u, T item) {
        return minimum <= u.getKarma();
    }

}
