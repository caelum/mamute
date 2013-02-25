package br.com.caelum.brutal.model;

import br.com.caelum.brutal.model.interfaces.Votable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class KarmaCalculator {

    public int karmaFor(VoteType type, Votable votable) {
        if (votable.getType() == Question.class) {
            return karmaForQuestion(type);
        } 
        return karmaForAnswer(type);
    }

    private int karmaForQuestion(VoteType type) {
        return type == VoteType.UP ? 5 : -2;
    }

    private int karmaForAnswer(VoteType type) {
        return type == VoteType.UP ? 10 : -2;
    }
    
}
