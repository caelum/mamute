package br.com.caelum.brutal.validators;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class UrlValidator {

	private String siteUrl;

	public UrlValidator(Environment env) {
		this.siteUrl = env.get("site.url");
	}

	public boolean isValid(String url) {
		if (url != null && !url.startsWith(siteUrl)) {
			return false;
		}
		return true;
	}
	
	
	
}
