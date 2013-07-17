package br.com.caelum.brutal.brutauth.reflection;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;

public interface BrutauthMethodSearcher {
	BrutauthMethod search(CustomBrutauthRule ruleToSearch, Object...withArgs);
}
