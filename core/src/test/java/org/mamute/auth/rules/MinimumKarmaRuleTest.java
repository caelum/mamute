package org.mamute.auth.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.vidageek.mirror.dsl.Mirror;

import org.junit.Test;
import org.mamute.auth.rules.MinimumKarmaRule;
import org.mamute.dao.TestCase;
import org.mamute.model.User;

public class MinimumKarmaRuleTest extends TestCase {
    private MinimumKarmaRule<Void> rule = new MinimumKarmaRule<>(10);

    @Test
    public void should_allow_user_with_enough_karma() {
        User u = user("user with 10 karma", "karma@brutal.com");
        setKarma(u, 10l);
        
        assertTrue(rule.isAllowed(u, null));
    }
    
    @Test
    public void should_disallow_user_with_low_karma() {
        User noob = user("noob", "noob@brutal.com");
        setKarma(noob, 1l);
        
        assertFalse(rule.isAllowed(noob, null));
    }

    private void setKarma(User u, long karma) {
        new Mirror().on(u).set().field("karma").withValue(karma);
    }
    
    

}
