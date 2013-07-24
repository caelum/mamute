package br.com.caelum.brutal.brutauth.auth.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import br.com.caelum.brutal.brutauth.auth.handlers.RuleHandler;


@Retention(RetentionPolicy.RUNTIME)
public @interface HandledBy {
	Class<? extends RuleHandler> value();
}
