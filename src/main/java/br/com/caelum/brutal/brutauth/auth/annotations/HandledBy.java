package br.com.caelum.brutal.brutauth.auth.annotations;

import br.com.caelum.brutal.brutauth.auth.handlers.RuleHandler;


public @interface HandledBy {
	Class<? extends RuleHandler> value();
}
