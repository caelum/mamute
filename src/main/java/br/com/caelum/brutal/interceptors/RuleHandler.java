package br.com.caelum.brutal.interceptors;

import br.com.caelum.brutal.auth.rules.BrutauthRule;

public interface RuleHandler {
	boolean canHandle(Class<? extends BrutauthRule> value);
	void handle(Class<? extends BrutauthRule> value);

}
