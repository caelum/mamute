package br.com.caelum.brutal.model;

import br.com.caelum.brutal.model.interfaces.Votable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class KarmaCalculator {

    public int karmaFor(VoteType type, Votable votable) {
        return type.getKarmaValue(votable.getType());
    }
    
}
