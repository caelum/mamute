package br.com.caelum.brutal.brutauth.reflection.methodsearchers;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutal.brutauth.reflection.BrutauthMethod;

public interface BrutauthMethodSearcher {
	BrutauthMethod search(CustomBrutauthRule ruleToSearch, Object...withArgs);
}
