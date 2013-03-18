package br.com.caelum.brutal.validators;

import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.I18nMessage;

@Component
public class UrlValidator {

	private String siteUrl;
	private final Validator validator;

	public UrlValidator(Environment env, Validator validator) {
		this.validator = validator;
		this.siteUrl = env.get("site.url");
	}

	public void validate(String url) {
		if (url != null && !url.startsWith(siteUrl)) {
			validator.add(new I18nMessage("error", "error.invalid.url", url));
		}
	}
	
	
	
}
