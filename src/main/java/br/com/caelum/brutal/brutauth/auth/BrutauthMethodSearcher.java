package br.com.caelum.brutal.brutauth.auth;

import java.lang.reflect.Method;

import br.com.caelum.brutal.brutauth.auth.rules.BrutauthRule;

public interface BrutauthMethodSearcher {
	Method getMethod(BrutauthRule toInvoke, Object...args);
}
