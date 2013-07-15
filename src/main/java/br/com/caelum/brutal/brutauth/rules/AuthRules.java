package br.com.caelum.brutal.brutauth.rules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.caelum.brutal.brutauth.auth.rules.BrutauthRule;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRules {
	Class<? extends BrutauthRule>[] value();
}
