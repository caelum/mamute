package org.mamute.brutauth.auth.rules;

import br.com.caelum.vraptor.environment.Environment;

import javax.inject.Inject;

public class EnvironmentKarma {

	private Environment environment;

	@Deprecated
	EnvironmentKarma() {
	}

	@Inject
	public EnvironmentKarma(Environment environment) {
		this.environment = environment;
	}

	public long get(String key) {
		String accessLevelString = environment.get("permission.rule." + key);
		long karma = Long.parseLong(accessLevelString);
		return karma;
	}
}
