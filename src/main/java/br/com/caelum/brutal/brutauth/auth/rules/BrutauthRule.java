package br.com.caelum.brutal.brutauth.auth.rules;


public interface BrutauthRule {

	boolean isAllowed(long accessLevel);
}
