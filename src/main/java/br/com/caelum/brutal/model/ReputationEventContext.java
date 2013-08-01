package br.com.caelum.brutal.model;

import br.com.caelum.brutal.model.interfaces.Votable;

public interface ReputationEventContext {
	Long getId();
	Class<? extends Votable> getType();
}
